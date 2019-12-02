package ren.crux.rainbow.core.reader.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.checkerframework.checker.nullness.qual.NonNull;
import ren.crux.rainbow.core.parser.*;
import ren.crux.rainbow.core.parser.impl.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    private AnnotationDocParser annotationDocParser = new DefaultAnnotationDocParser();
    private DescriptionDocParser descriptionDocParser = new DefaultDescriptionDocParser();
    private RefDocParser refDocParser = new DefaultRefDocParser();
    private EntryDocParser entryDocParser = new DefaultEntryDocParser();
    private EntryFieldDocParser entryFieldDocParser = new DefaultEntryFieldDocParser();
    private RequestDocParser requestDocParser = new DefaultRequestDocParser();
    private RequestMappingDocParser requestMappingDocParser = new SpringWebRequestMappingDocParser();
    private RequestGroupDocParser requestGroupDocParser = new SpringWebRequestGroupDocParser();
    private RequestParameterDocParser requestParameterDocParser = new SpringWebRequestParameterDocParser();
    private Set<String> types = new HashSet<>();


    public void logType(@NonNull String type) {
        types.add(type);
    }

    public Set<String> getTypes() {
        return types;
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

    public AnnotationDocParser getAnnotationDocParser() {
        return annotationDocParser;
    }

    public void setAnnotationDocParser(AnnotationDocParser annotationDocParser) {
        this.annotationDocParser = annotationDocParser;
    }

    public DescriptionDocParser getDescriptionDocParser() {
        return descriptionDocParser;
    }

    public void setDescriptionDocParser(DescriptionDocParser descriptionDocParser) {
        this.descriptionDocParser = descriptionDocParser;
    }

    public RefDocParser getRefDocParser() {
        return refDocParser;
    }

    public void setRefDocParser(RefDocParser refDocParser) {
        this.refDocParser = refDocParser;
    }

    public EntryDocParser getEntryDocParser() {
        return entryDocParser;
    }

    public void setEntryDocParser(EntryDocParser entryDocParser) {
        this.entryDocParser = entryDocParser;
    }

    public EntryFieldDocParser getEntryFieldDocParser() {
        return entryFieldDocParser;
    }

    public void setEntryFieldDocParser(EntryFieldDocParser entryFieldDocParser) {
        this.entryFieldDocParser = entryFieldDocParser;
    }

    public RequestDocParser getRequestDocParser() {
        return requestDocParser;
    }

    public void setRequestDocParser(RequestDocParser requestDocParser) {
        this.requestDocParser = requestDocParser;
    }

    public RequestMappingDocParser getRequestMappingDocParser() {
        return requestMappingDocParser;
    }

    public void setRequestMappingDocParser(RequestMappingDocParser requestMappingDocParser) {
        this.requestMappingDocParser = requestMappingDocParser;
    }

    public RequestGroupDocParser getRequestGroupDocParser() {
        return requestGroupDocParser;
    }

    public void setRequestGroupDocParser(RequestGroupDocParser requestGroupDocParser) {
        this.requestGroupDocParser = requestGroupDocParser;
    }

    public RequestParameterDocParser getRequestParameterDocParser() {
        return requestParameterDocParser;
    }

    public void setRequestParameterDocParser(RequestParameterDocParser requestParameterDocParser) {
        this.requestParameterDocParser = requestParameterDocParser;
    }
}
