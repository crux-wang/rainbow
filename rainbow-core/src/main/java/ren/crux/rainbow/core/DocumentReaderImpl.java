package ren.crux.rainbow.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.module.ModuleBuilder;
import ren.crux.rainbow.core.parser.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DocumentReaderImpl implements DocumentReader {

    private final ClassDocProvider classDocProvider;
    private final RequestGroupProvider requestGroupProvider;
    private final Map<String, Object> properties;
    private final Map<String, String> implMap;
    private EntryParser entryParser;
    private EntryFieldParser entryFieldParser;
    private EntryMethodParser entryMethodParser;
    private AnnotationParser annotationParser;
    private CommentTextParser commentTextParser;
    private RequestDocParser requestDocParser;
    private RequestGroupDocParser requestGroupDocParser;
    private RequestParamDocParser requestParamDocParser;

    public DocumentReaderImpl(ClassDocProvider classDocProvider,
                              RequestGroupProvider requestGroupProvider,
                              Map<String, Object> properties, Map<String, String> implMap, List<Module> modules) {
        this.classDocProvider = classDocProvider;
        this.requestGroupProvider = requestGroupProvider;
        this.properties = properties;
        this.implMap = implMap;
        if (CollectionUtils.isNotEmpty(modules)) {
            loadModels(modules);
        }
    }

    private void loadModels(List<Module> modules) {
        ModuleBuilder combinationModuleBuilder = new ModuleBuilder().name("combination module");
        for (Module module : modules) {
            combinationModuleBuilder
                    .entry().interceptor(module.entry()).end()
                    .entryField().interceptor(module.entryField()).end()
                    .entryMethod().interceptor(module.entryMethod()).end()
                    .annotation().interceptor(module.annotation()).end()
                    .commentText().interceptor(module.commentText()).end()
                    .requestGroup().interceptor(module.requestGroup()).end()
                    .request().interceptor(module.request()).end()
                    .requestParam().interceptor(module.requestParam()).end();
        }
        Module combinationModule = combinationModuleBuilder.build();
        annotationParser = new AnnotationParser(combinationModule.annotation());
        commentTextParser = new CommentTextParser(combinationModule.commentText());
        entryFieldParser = new EntryFieldParser(combinationModule.entryField(), annotationParser, commentTextParser);
        entryMethodParser = new EntryMethodParser(combinationModule.entryMethod(), annotationParser, commentTextParser);
        entryParser = new EntryParser(combinationModule.entry(), entryFieldParser, entryMethodParser, annotationParser, commentTextParser);
        requestParamDocParser = new RequestParamDocParser(combinationModule.requestParam());
        requestDocParser = new RequestDocParser(combinationModule.request(), commentTextParser, requestParamDocParser);
        requestGroupDocParser = new RequestGroupDocParser(combinationModule.requestGroup(), commentTextParser, requestDocParser);
    }

    protected Context newContext() {
        Context context = new Context(classDocProvider);
        context.getProperties().putAll(properties);
        context.getImplMap().putAll(implMap);
        classDocProvider.setUp(context);
        return context;
    }

    /**
     * 读取
     *
     * @return 文档
     */
    @Override
    public DocumentStream read() {
        Context context = newContext();
        List<RequestGroup> requestGroups = requestGroupProvider.get(context);
        requestGroups = requestGroupDocParser.parse(context, requestGroups);
        Set<String> entryClassNames = context.getEntryClassNames();
        List<Class<?>> entryClasses = map2Class(entryClassNames);
        Map<String, Entry> entryMap = entryParser.parse(context, entryClasses).stream().collect(Collectors.toMap(Entry::getType, e -> e));
        // 二次处理新出现的实体
        entryClassNames = context.getEntryClassNames();
        if (entryClassNames.removeAll(entryMap.keySet())) {
            entryClasses = map2Class(entryClassNames);
            Map<String, Entry> extraEntryMap = entryParser.parse(context, entryClasses).stream().collect(Collectors.toMap(Entry::getType, e -> e));
            entryMap.putAll(extraEntryMap);
        }
        Document document = new Document();
        document.setRequestGroups(requestGroups);
        document.setEntryMap(entryMap);
        document.getProperties().putAll(context.getProperties());
        return new DocumentStream(document);
    }

    List<Class<?>> map2Class(Collection<String> classNames) {
        return classNames.stream().map(className -> {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                log.error("class not found : {}", className, e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
