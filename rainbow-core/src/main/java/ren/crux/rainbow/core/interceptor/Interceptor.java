package ren.crux.rainbow.core.interceptor;

import ren.crux.rainbow.core.module.Context;

/**
 * 拦截器
 *
 * @param <S> 源
 * @param <T> 目标
 * @author wangzhihui
 */
public interface Interceptor<S, T> {

    /**
     * 之前
     *
     * @param context 上下文
     * @param source  源
     * @return 是否继续
     */
    default boolean before(Context context, S source) {
        return true;
    }

    /**
     * 之后
     *
     * @param context 上下文
     * @param target  目标
     * @return 是否继续
     */
    default boolean after(Context context, T target) {
        return true;
    }

    /**
     * 顺序
     *
     * @return 顺序
     */
    default int order() {
        return 0;
    }

}
