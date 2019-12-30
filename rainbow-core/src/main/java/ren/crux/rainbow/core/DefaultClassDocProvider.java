package ren.crux.rainbow.core;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ProgramElementDoc;
import ren.crux.rainbow.core.filter.CombinationFilter;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.option.Option;
import ren.crux.rainbow.javadoc.reader.DefaultJavaDocReader;
import ren.crux.rainbow.javadoc.reader.JavaDocReader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
public class DefaultClassDocProvider implements ClassDocProvider {

    public static final Option<String[]> SOURCE_PATH = Option.valueOf("SOURCE_PATH");
    public static final Option<String[]> PACKAGES = Option.valueOf("PACKAGES");

    private String[] sourcePath;
    private String[] packages;
    private CombinationFilter.CombinationFilterBuilder<ClassDoc> builder = CombinationFilter.builder();
    private JavaDocReader<List<ClassDoc>> javaDocReader;
    private Map<String, ClassDoc> classDocMap;
    private DocumentReaderBuilder owner;
    private ClassDoc classDoc;

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
            context.setOption(SOURCE_PATH, sourcePath);
            context.setOption(PACKAGES, packages);
            javaDocReader = new DefaultJavaDocReader();
            CombinationFilter<ClassDoc> filters = builder.build();
            classDocMap = javaDocReader.read(sourcePath, packages)
                    .orElse(Collections.emptyList())
                    .stream().filter(cd -> filters.include(context, cd))
                    .collect(Collectors.toMap(ProgramElementDoc::qualifiedName, cd -> cd));
            if (!classDocMap.isEmpty()) {
                classDocMap.values().stream().findFirst().ifPresent(this::setClassDoc);
            }
        }
    }

    private void setClassDoc(ClassDoc classDoc) {
        this.classDoc = classDoc;
    }

    /**
     * 返回任意一个类文档描述
     *
     * @return 类文档描述
     */
    @Override
    public Optional<ClassDoc> any() {
        return Optional.ofNullable(classDoc);
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
