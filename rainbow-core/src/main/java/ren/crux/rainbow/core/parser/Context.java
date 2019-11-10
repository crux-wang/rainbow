package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.checkerframework.checker.nullness.qual.NonNull;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.Link;
import ren.crux.rainbow.core.parser.impl.RestControllerParser;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public interface Context {

    String OBJECT_TYPE_NAME = Object.class.getTypeName();

    /**
     * 获取根文档
     *
     * @return 根文档
     */
    @NonNull
    RootDoc getRootDoc();

    /**
     * 获取类文档
     *
     * @param qualifiedName 限定名
     * @return 类文档
     */
    Optional<ClassDoc> findClass(@NonNull String qualifiedName);

    /**
     * 是否是 REST Controller
     *
     * @param classDoc 类文档
     * @return 是否是 REST Controller
     */
    default boolean isRestController(ClassDoc classDoc) {
        return false;
    }

    /**
     * 是否是实体类
     *
     * @param classDoc 类文档
     * @return 是否是实体类
     */
    default boolean isEntry(ClassDoc classDoc) {
        return false;
    }

    /**
     * 获取链接
     *
     * @param entry 实体
     * @return 链接
     */
    Link getLink(Entry entry);

    /**
     * 记录实体
     *
     * @param entry 实体
     */
    void logEntry(Entry entry);

    /**
     * 获取实体
     *
     * @param qualifiedName 限定名
     * @return 实体
     */
    Optional<Entry> getEntry(@NonNull String qualifiedName);

    /* getter */

    Optional<FieldDocParser> getFieldDocParser();

    void setFieldDocParser(FieldDocParser fieldDocParser);

    Optional<EntryDocParser> getEntryDocParser();

    void setEntryDocParser(EntryDocParser entryParser);

//     ParameterParser getParameterParser();

    Optional<RequestDocParser> getRequestDocParser();

    /* setter */

    void setRequestDocParser(RequestDocParser requestParser);

    Optional<RestControllerParser> getRestControllerParser();

    void setRestControllerParser(RestControllerParser restControllerParser);

    Optional<TagDocParser> getTagDocParser();

    void setTagDocParser(TagDocParser tagDocParser);
}
