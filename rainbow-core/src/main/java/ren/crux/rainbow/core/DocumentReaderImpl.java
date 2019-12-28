package ren.crux.rainbow.core;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.module.CombinationModule;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.option.RevisableConfig;
import ren.crux.rainbow.core.parser.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
@Slf4j
public class DocumentReaderImpl implements DocumentReader {

    private final ClassDocProvider classDocProvider;
    private final RequestGroupProvider requestGroupProvider;
    private final RevisableConfig config;
    private final Map<String, String> implMap;
    private EntryParser entryParser;
    private EntryFieldParser entryFieldParser;
    private EntryMethodParser entryMethodParser;
    private AnnotationParser annotationParser;
    private CommentTextParser commentTextParser;
    private RequestDocParser requestDocParser;
    private RequestGroupDocParser requestGroupDocParser;
    private RequestParamDocParser requestParamDocParser;
    private Context context;

    public DocumentReaderImpl(@NonNull ClassDocProvider classDocProvider,
                              @NonNull RequestGroupProvider requestGroupProvider,
                              @NonNull RevisableConfig config, @NonNull Map<String, String> implMap, @NonNull List<Module> modules) {
        this.classDocProvider = classDocProvider;
        this.requestGroupProvider = requestGroupProvider;
        this.config = config;
        this.implMap = implMap;
        loadModels(modules);
    }

    private void loadModels(List<Module> modules) {
        CombinationModule combinationModule = new CombinationModule(modules);
        context = newContext();
        combinationModule.setUp(context);
        implMap.putAll(combinationModule.implMap());
        annotationParser = new AnnotationParser(combinationModule.annotation().orElse(null));
        commentTextParser = new CommentTextParser(combinationModule.commentText().orElse(null));
        entryFieldParser = new EntryFieldParser(combinationModule.entryField().orElse(null), annotationParser, commentTextParser);
        entryMethodParser = new EntryMethodParser(combinationModule.entryMethod().orElse(null), annotationParser, commentTextParser);
        entryParser = new EntryParser(combinationModule.entry().orElse(null), entryFieldParser, entryMethodParser, annotationParser, commentTextParser);
        requestParamDocParser = new RequestParamDocParser(combinationModule.requestParam().orElse(null));
        requestDocParser = new RequestDocParser(combinationModule.request().orElse(null), commentTextParser, requestParamDocParser);
        requestGroupDocParser = new RequestGroupDocParser(combinationModule.requestGroup().orElse(null), commentTextParser, requestDocParser);

    }

    protected Context newContext() {
        Context context = new Context(config, implMap, classDocProvider);
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
        document.getProperties().putAll(context.getConfig().asMap());
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
