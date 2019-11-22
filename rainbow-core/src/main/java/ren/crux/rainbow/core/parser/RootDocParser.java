package ren.crux.rainbow.core.parser;

import com.sun.javadoc.RootDoc;

/**
 * 根解析器
 *
 * @author wangzhihui
 */
public interface RootDocParser<T> extends JavaDocParser<RootDoc, T> {

    @Override
    default boolean support(Context context, RootDoc source) {
        return true;
    }

}
