package ren.crux.rainbow.common;

/**
 * 拦截器
 *
 * @param <S> 源
 * @param <T> 目标
 * @param <C> 上下文
 * @author wangzhihui
 */
public interface Interceptor<S, T, C> {

    /**
     * 之前
     *
     * @param context 上下文
     * @param source  源
     * @return 是否继续
     */
    default boolean before(C context, S source) {
        return true;
    }

    /**
     * 之后
     *
     * @param context 上下文
     * @param target  目标
     * @return 是否继续
     */
    default boolean after(C context, T target) {
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
