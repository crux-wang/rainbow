package ren.crux.rainbow.core.reader.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.checkerframework.checker.nullness.qual.NonNull;
import ren.crux.rainbow.core.reader.parser.filter.ClassDocFilter;
import ren.crux.rainbow.core.reader.parser.filter.FieldDocFilter;
import ren.crux.rainbow.core.reader.parser.filter.MethodDocFilter;

import java.util.Objects;
import java.util.Optional;

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
    private ClassDocFilter classDocFilter = new ClassDocFilter();
    private FieldDocFilter fieldDocFilter = new FieldDocFilter();
    private MethodDocFilter methodDocFilter = new MethodDocFilter();

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

    public ClassDocFilter getClassDocFilter() {
        return classDocFilter;
    }

    public void setClassDocFilter(ClassDocFilter classDocFilter) {
        this.classDocFilter = classDocFilter;
    }

    public FieldDocFilter getFieldDocFilter() {
        return fieldDocFilter;
    }

    public void setFieldDocFilter(FieldDocFilter fieldDocFilter) {
        this.fieldDocFilter = fieldDocFilter;
    }

    public MethodDocFilter getMethodDocFilter() {
        return methodDocFilter;
    }

    public void setMethodDocFilter(MethodDocFilter methodDocFilter) {
        this.methodDocFilter = methodDocFilter;
    }
}
