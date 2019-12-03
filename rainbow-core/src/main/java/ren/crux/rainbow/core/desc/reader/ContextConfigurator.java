package ren.crux.rainbow.core.desc.reader;

import ren.crux.rainbow.core.desc.reader.parser.Context;

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
    }
}
