package ren.crux.rainbow.core.parser.module;

import ren.crux.rainbow.core.ContextConfigurer;

/**
 * @author wangzhihui
 */
public interface Module extends ContextConfigurer {

    /**
     * 顺序
     *
     * @return 序号
     */
    default int order() {
        return 0;
    }

    /**
     * 模块名
     *
     * @return 模块名
     */
    String name();

}
