package ren.crux.rainbow.core.filter;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

/**
 * 组合过滤器
 *
 * @param <T> 过滤目标类型
 * @author wangzhihui
 */
@Builder(toBuilder = true)
public class CombinationFilter<T, C> implements Filter<T, C> {

    /**
     * 过滤器列表
     */
    @Singular
    protected final List<Filter<T, C>> filters;

    public CombinationFilter(@NonNull List<Filter<T, C>> filters) {
        this.filters = filters;
    }

    /**
     * 包含
     *
     * @param context 上下文
     * @param t       目标
     * @return 是否包含目标
     */
    @Override
    public boolean include(C context, T t) {
        return filters.isEmpty() || filters.stream().allMatch(i -> i.include(context, t));
    }
}
