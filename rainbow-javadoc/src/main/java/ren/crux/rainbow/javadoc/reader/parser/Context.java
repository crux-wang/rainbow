package ren.crux.rainbow.javadoc.reader.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.RootDoc;
import lombok.NonNull;
import ren.crux.rainbow.javadoc.reader.parser.filter.ClassDocFilter;
import ren.crux.rainbow.javadoc.reader.parser.filter.FieldDocFilter;
import ren.crux.rainbow.javadoc.reader.parser.filter.MethodDocFilter;

import java.util.*;

/**
 * 上下文
 *
 * @author wangzhihui
 */
public class Context {

    public static final String OBJECT_TYPE_NAME = Object.class.getTypeName();
    private final RootDoc rootDoc;
    private final ClassDoc classDoc;
    private final String path;
    private final String[] packageNames;
    private final List<ClassDocFilter> classDocFilters = new LinkedList<>();
    private final List<FieldDocFilter> fieldDocFilters = new LinkedList<>();
    private final List<MethodDocFilter> methodDocFilters = new LinkedList<>();

    public void addFilter(ClassDocFilter... filters) {
        if (filters != null) {
            classDocFilters.addAll(Arrays.asList(filters));
        }
    }

    public void addFilter(FieldDocFilter... filters) {
        if (filters != null) {
            fieldDocFilters.addAll(Arrays.asList(filters));
        }
    }

    public void addFilter(MethodDocFilter... filters) {
        if (filters != null) {
            methodDocFilters.addAll(Arrays.asList(filters));
        }
    }

    public boolean doFilter(ClassDoc classDoc) {
        return classDocFilters.isEmpty() || classDocFilters.stream().allMatch(f -> f.doFilter(classDoc));
    }

    public boolean doFilter(FieldDoc fieldDoc) {
        return fieldDocFilters.isEmpty() || fieldDocFilters.stream().allMatch(f -> f.doFilter(fieldDoc));
    }

    public boolean doFilter(MethodDoc methodDoc) {
        return methodDocFilters.isEmpty() || methodDocFilters.stream().allMatch(f -> f.doFilter(methodDoc));
    }

    private Context(RootDoc rootDoc, String path, String[] packageNames) {
        this.rootDoc = rootDoc;
        this.path = path;
        this.packageNames = packageNames;
        ClassDoc[] classDocs = rootDoc.classes();
        Objects.requireNonNull(classDocs);
        if (classDocs.length > 0) {
            this.classDoc = classDocs[0];
        } else {
            this.classDoc = null;
        }
    }

    public static Context newContext(RootDoc rootDoc, String path, String[] packageNames) {
        return new Context(rootDoc, path, packageNames);
    }

    /**
     * 获取根文档
     *
     * @return 根文档
     */
    public @NonNull RootDoc getRootDoc() {
        return rootDoc;
    }

    /**
     * 获取类文档
     *
     * @param className 类名
     * @return 类文档
     */
    public Optional<ClassDoc> findClass(@NonNull String className) {
        return Optional.ofNullable(classDoc).map(cd -> cd.findClass(className));
    }

    public Optional<ClassDoc> getClassDoc() {
        return Optional.ofNullable(classDoc);
    }

    public String getPath() {
        return path;
    }

    public String[] getPackageNames() {
        return packageNames;
    }

}
