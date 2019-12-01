package ren.crux.rainbow.core.reader.parser;

import com.sun.javadoc.RootDoc;

/**
 * 根文档解析器
 *
 * @author wangzhihui
 */
public interface RootDocParser<T> extends JavaDocParser<RootDoc, T> {
    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    default boolean support(Context context, RootDoc source) {
        return true;
    }

}
