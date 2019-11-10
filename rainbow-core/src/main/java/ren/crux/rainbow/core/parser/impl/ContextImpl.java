package ren.crux.rainbow.core.parser.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.Link;
import ren.crux.rainbow.core.parser.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * @author wangzhihui
 */
@Setter
public class ContextImpl implements Context {

    private final RootDoc rootDoc;
    private final ClassDoc classDoc;
    private final Cache<String, Link> linkCache = CacheBuilder.newBuilder().build();
    private final Map<String, Entry> entryMap = new HashMap<>();
    private FieldDocParser fieldDocParser;
    private EntryDocParser entryDocParser;
    private RequestDocParser requestDocParser;
    private TagDocParser tagDocParser;
    private RestControllerParser restControllerParser;

    public ContextImpl(RootDoc rootDoc) {
        this.rootDoc = rootDoc;
        ClassDoc[] classDocs = rootDoc.classes();
        Objects.requireNonNull(classDocs);
        if (classDocs.length > 0) {
            this.classDoc = classDocs[0];
        } else {
            this.classDoc = null;
        }
    }

    /**
     * 获取链接
     *
     * @param entry 实体
     * @return 链接
     */
    @Override
    public Link getLink(Entry entry) {
        try {
            return linkCache.get(entry.getQualifiedName(), () -> {
                Link ref = new Link();
                ref.setName(entry.getName());
                ref.setTarget(entry.getQualifiedName());
                ref.setTag("#Link");
                return ref;
            });
        } catch (ExecutionException e) {
            return null;
        }
    }

    /**
     * 记录实体
     *
     * @param entry 实体
     */
    @Override
    public void logEntry(Entry entry) {
        entryMap.put(entry.getQualifiedName(), entry);
    }

    /**
     * 获取实体
     *
     * @param qualifiedName 限定名
     * @return 实体
     */
    @Override
    public Optional<Entry> getEntry(@NonNull String qualifiedName) {
        return Optional.ofNullable(entryMap.get(qualifiedName));
    }


    /**
     * 获取根文档
     *
     * @return 根文档
     */
    @Override
    public @NonNull RootDoc getRootDoc() {
        return rootDoc;
    }

    /**
     * 获取类文档
     *
     * @param className 类名
     * @return 类文档
     */
    @Override
    public Optional<ClassDoc> findClass(@NonNull String className) {
        return Optional.ofNullable(classDoc).map(cd -> cd.findClass(className));
    }

    @Override
    public Optional<FieldDocParser> getFieldDocParser() {
        return Optional.ofNullable(fieldDocParser);
    }

    @Override
    public Optional<EntryDocParser> getEntryDocParser() {
        return Optional.ofNullable(entryDocParser);
    }

    @Override
    public Optional<RequestDocParser> getRequestDocParser() {
        return Optional.ofNullable(requestDocParser);
    }

    @Override
    public Optional<RestControllerParser> getRestControllerParser() {
        return Optional.ofNullable(restControllerParser);
    }

    @Override
    public Optional<TagDocParser> getTagDocParser() {
        return Optional.ofNullable(tagDocParser);
    }

    public Optional<ClassDoc> getClassDoc() {
        return Optional.ofNullable(classDoc);
    }
}
