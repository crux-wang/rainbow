package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.reader.parser.ClassDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.utils.DocHelper;

/**
 * 实体文档解析器
 *
 * @author wangzhihui
 */
public interface EntryDocParser extends ClassDocParser<Entry> {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    default boolean support(@NonNull Context context, @NonNull ClassDoc source) {
        return DocHelper.hasAnyFieldDoc(source);
    }

}
