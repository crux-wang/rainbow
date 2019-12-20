package ren.crux.rainbow.javadoc.reader;

import ren.crux.rainbow.javadoc.reader.parser.Context;
import ren.crux.rainbow.javadoc.reader.parser.filter.DefaultClassDocFilter;
import ren.crux.rainbow.javadoc.reader.parser.filter.DefaultMethodDocFilter;

/**
 * 上下文配置器
 *
 * @author wangzhihui
 */
public interface ContextConfigurator {

    /**
     * 配置
     *
     * @param context 上下文
     */
    default void config(Context context) {
        context.addFilter(new DefaultClassDocFilter());
        context.addFilter(new DefaultMethodDocFilter());
    }
}
