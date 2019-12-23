package ren.crux.rainbow.core;

import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.module.Module;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DocumentReaderImpl implements DocumentReader {

    private final ClassDocProvider classDocProvider;
    private final RequestGroupProvider requestGroupProvider;
    private final Map<String, Object> properties;
    private final Map<String, String> implMap;
    private final List<Module> modules;
//    private final EntryParser entryParser;
//    private final EntryFieldParser entryFieldParser;
//    private final EntryMethodParser entryMethodParser;
//    private final AnnotationParser annotationParser;
//    private final CommentTextParser commentTextParser;

    public DocumentReaderImpl(ClassDocProvider classDocProvider,
                              RequestGroupProvider requestGroupProvider,
                              Map<String, Object> properties, Map<String, String> implMap, List<Module> modules) {
        this.classDocProvider = classDocProvider;
        this.requestGroupProvider = requestGroupProvider;
        this.properties = properties;
        this.implMap = implMap;
        this.modules = modules;
//        this.annotationParser = new AnnotationParser();
    }

    /**
     * 读取
     *
     * @return 文档
     */
    @Override
    public Optional<Document> read() {
        return Optional.empty();
    }
}
