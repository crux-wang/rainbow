package ren.crux.rainbow.core;

import ren.crux.rainbow.javadoc.model.ClassDesc;
import ren.crux.rainbow.javadoc.reader.ContextConfigurator;
import ren.crux.rainbow.javadoc.reader.JavaDocReader;
import ren.crux.rainbow.javadoc.reader.impl.DefaultJavaDocReader;
import ren.crux.rainbow.javadoc.reader.impl.DefaultRootDocParser;
import ren.crux.rainbow.javadoc.reader.parser.RootDocParser;
import ren.crux.rainbow.javadoc.reader.parser.filter.ClassDocFilter;
import ren.crux.rainbow.javadoc.reader.parser.filter.FieldDocFilter;
import ren.crux.rainbow.javadoc.reader.parser.filter.MethodDocFilter;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultClassDescProvider implements ClassDescProvider {

    private String sourcePath;
    private String[] packages;
    private ClassDocFilter[] classDocFilters;
    private FieldDocFilter[] fieldDocFilters;
    private MethodDocFilter[] methodDocFilters;
    private Map<String, ClassDesc> classDescMap;
    private DocumentReader owner;

    @Override
    public ClassDescProvider source(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    @Override
    public ClassDescProvider packages(String... packages) {
        this.packages = packages;
        return this;
    }

    @Override
    public ClassDescProvider filter(ClassDocFilter... filters) {
        this.classDocFilters = filters;
        return this;
    }

    @Override
    public ClassDescProvider filter(FieldDocFilter... filters) {
        this.fieldDocFilters = filters;
        return this;
    }

    @Override
    public ClassDescProvider filter(MethodDocFilter... filters) {
        this.methodDocFilters = filters;
        return this;
    }

    @Override
    public void setUp(Context context) {
        Objects.requireNonNull(sourcePath);
        RootDocParser<List<ClassDesc>> rootParser = new DefaultRootDocParser();
        JavaDocReader<List<ClassDesc>> javaDocReader = new DefaultJavaDocReader(rootParser, new ContextConfigurator() {
            @Override
            public void config(ren.crux.rainbow.javadoc.reader.parser.Context context) {
                context.addFilter(classDocFilters);
                context.addFilter(fieldDocFilters);
                context.addFilter(methodDocFilters);
            }
        });
        classDescMap = javaDocReader.read(sourcePath, packages)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(ClassDesc::getType, c -> c));
        ClassDesc classDesc = classDescMap.get("ren.crux.rainbow.test.demo.model.User");
        System.out.println("classDesc = " + classDesc);
    }

    @Override
    public Optional<ClassDesc> get(Context context, String className) {
        return Optional.ofNullable(classDescMap).map(m -> m.get(className));
    }

    @Override
    public Map<String, ClassDesc> all() {
        return classDescMap;
    }

    @Override
    public void owner(DocumentReader reader) {
        owner = reader;
    }

    @Override
    public DocumentReader end() {
        return owner;
    }
}
