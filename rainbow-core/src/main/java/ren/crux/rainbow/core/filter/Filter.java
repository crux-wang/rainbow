package ren.crux.rainbow.core.filter;

import ren.crux.rainbow.core.module.Context;

/**
 * 过滤器
 *
 * @param <T> 过滤目标类型
 * @author wangzhihui
 */
public interface Filter<T> {

    /**
     * 包含
     *
     * @param context 上下文
     * @param t       目标
     * @return 是否包含目标
     */
    default boolean include(Context context, T t) {
        return true;
    }

}
