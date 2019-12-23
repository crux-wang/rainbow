package ren.crux.rainbow.core.filter;

/**
 * 过滤器
 *
 * @param <T> 过滤目标类型
 * @param <C> 上下文类型
 * @author wangzhihui
 */
public interface Filter<T, C> {

    /**
     * 包含
     *
     * @param context 上下文
     * @param t       目标
     * @return 是否包含目标
     */
    default boolean include(C context, T t) {
        return true;
    }

}
