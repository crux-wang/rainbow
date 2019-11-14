package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import ren.crux.rainbow.core.model.Document;

/**
 * 类解析器
 *
 * @author wangzhihui
 */
public interface ClassDocParser<T extends DocumentPart> extends JavaDocParser<ClassDoc, T> {


    /**
     * 解析并将结果合并到最终文档
     *
     * @param context  上下文
     * @param source   类文档
     * @param document 最终文档
     */
    default void parseAndMerge(Context context, ClassDoc source, Document document) {
        if (support(context, source)) {
            parse(context, source).ifPresent(t -> t.merge(document));
        }
    }

}