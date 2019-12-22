package ren.crux.rainbow.core;

import com.sun.javadoc.ClassDoc;
import ren.crux.rainbow.common.CombinationFilter;
import ren.crux.rainbow.common.Filter;
import ren.crux.rainbow.javadoc.reader.DefaultJavaDocReader;
import ren.crux.rainbow.javadoc.reader.JavaDocReader;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AbstractClassDocProvider<T, C> implements ClassDocProvider<T, C> {

    private String sourcePath;
    private String[] packages;
    private CombinationFilter.CombinationFilterBuilder<T, C> builder;
    private Map<String, ClassDoc> classDocMap;
    private DocumentReader owner;


    public ClassDocProvider<T, C> source(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }


    public ClassDocProvider<T, C> packages(String... packages) {
        this.packages = packages;
        return this;
    }


    public ClassDocProvider<T, C> filter(Filter<T, C>... filters) {
        if (filters != null) {
            for (Filter<T, C> filter : filters) {
                this.builder.filter(filter);
            }
        }
        return this;
    }

    @Override
    public void setUp(Context context) {
        Objects.requireNonNull(sourcePath);
        JavaDocReader<Map<String, ClassDoc>> javaDocReader = new DefaultJavaDocReader();
        classDocMap = javaDocReader.read(sourcePath, packages)
                .orElse(Collections.emptyMap());
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
