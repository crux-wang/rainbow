package ren.crux.rainbow.core;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import ren.crux.rainbow.core.filter.CombinationFilter;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.javadoc.reader.DefaultJavaDocReader;
import ren.crux.rainbow.javadoc.reader.JavaDocReader;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultClassDocProvider implements ClassDocProvider {

    public static final String SOURCE_PATH = "SOURCE_PATH";
    public static final String PACKAGES = "PACKAGES";

    private String[] sourcePath;
    private String[] packages;
    private CombinationFilter.CombinationFilterBuilder<ClassDoc, Context> builder = CombinationFilter.builder();
    private JavaDocReader<List<ClassDoc>> javaDocReader;
    private Map<String, ClassDoc> classDocMap;
    private DocumentReaderBuilder owner;

    public DefaultClassDocProvider source(String... sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public DefaultClassDocProvider packages(String... packages) {
        this.packages = packages;
        return this;
    }

    public DefaultClassDocProvider filter(ClassDocFilter... filters) {
        if (filters != null) {
            for (ClassDocFilter filter : filters) {
                this.builder.filter(filter);
            }
        }
        return this;
    }

    @Override
    public void setUp(Context context) {
        if (javaDocReader == null) {
            Objects.requireNonNull(sourcePath);
            context.property(SOURCE_PATH, sourcePath);
            context.property(PACKAGES, packages);
            javaDocReader = new DefaultJavaDocReader();
            CombinationFilter<ClassDoc, Context> filters = builder.build();
            classDocMap = javaDocReader.read(sourcePath, packages)
                    .orElse(Collections.emptyList())
                    .stream().filter(cd -> filters.include(context, cd))
                    .collect(Collectors.toMap(ProgramElementDoc::qualifiedName, cd -> cd));
        }
    }

    @Override
    public Optional<ClassDoc> get(Context context, String className) {
        return Optional.ofNullable(classDocMap).map(m -> m.get(className));
    }

    @Override
    public void owner(DocumentReaderBuilder reader) {
        owner = reader;
    }

    @Override
    public DocumentReaderBuilder end() {
        return owner;
    }
}
