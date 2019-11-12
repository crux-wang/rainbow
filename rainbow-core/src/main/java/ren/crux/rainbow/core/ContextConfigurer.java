package ren.crux.rainbow.core;

import ren.crux.rainbow.core.parser.Context;

/**
 * @author wangzhihui
 */
public interface ContextConfigurer {

    /**
     * 配置上下文
     *
     * @param context 上下文
     */
    default void configure(Context context) {
    }

}
