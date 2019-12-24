package ren.crux.rainbow.core;

import com.sun.javadoc.ClassDoc;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;
import ren.crux.rainbow.core.module.ModuleBuilder;
import ren.crux.rainbow.core.parser.*;

import java.util.*;
import java.util.stream.Collectors;

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
    public Optional<Document> read() {
        Context context = newContext();
        List<RequestGroup> requestGroups = requestGroupProvider.get(context);
        requestGroups = requestGroupDocParser.parse(context, buildRequestGroupClassDocPairs(context, requestGroups));
        Set<String> entryClassNames = context.getEntryClassNames();
        Pair<Class<?>, ClassDoc>[] classClassDocPairs = buildClassClassDocPairs(context, entryClassNames);
        Map<String, Entry> entryMap = entryParser.parse(context, classClassDocPairs).stream().collect(Collectors.toMap(Entry::getType, e -> e));
        Document document = new Document();
        document.setRequestGroups(requestGroups);
        document.setEntryMap(entryMap);
        document.getProperties().putAll(context.getProperties());
        return Optional.of(document);
    }

    @SuppressWarnings("unchecked")
    private Pair<Class<?>, ClassDoc>[] buildClassClassDocPairs(Context context, Collection<String> entryClassNames) {
        return entryClassNames.stream()
                .map(cln -> {
                    try {
                        Class<?> cls = Class.forName(cln);
                        return Pair.of(cls, context.getClassDoc(cls.getTypeName()).orElse(null));
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).toArray(Pair[]::new);
    }

    @SuppressWarnings("unchecked")
    private Pair<RequestGroup, ClassDoc>[] buildRequestGroupClassDocPairs(Context context, List<RequestGroup> requestGroups) {
        return requestGroups.stream()
                .map(rg -> Pair.of(rg, context.getClassDoc(rg.getType()).orElse(null))).toArray(Pair[]::new);
    }
}
