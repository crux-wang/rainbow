package ren.crux.rainbow.common;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

import java.util.Comparator;
import java.util.List;

/**
 * 组合解析器
 *
 * @param <S> 源
 * @param <T> 目标
 * @param <C> 上下文
 * @author wangzhihui
 */
@Builder(toBuilder = true)
public class CombinationInterceptor<S, T, C> implements Interceptor<S, T, C> {

    /**
     * 顺序
     */
    protected final int order;
    /**
     * 拦截器列表
     */
    @Singular
    protected final List<Interceptor<S, T, C>> interceptors;

    public CombinationInterceptor(@NonNull List<Interceptor<S, T, C>> interceptors) {
        this(0, interceptors);
    }

    public CombinationInterceptor(int order, @NonNull List<Interceptor<S, T, C>> interceptors) {
        this.order = order;
        this.interceptors = interceptors;
        this.interceptors.sort(Comparator.comparingInt(Interceptor::order));
    }

    /**
     * 之前
     *
     * @param context 上下文
     * @param source  源
     * @return 是否继续
     */
    @Override
    public boolean before(C context, S source) {
        return interceptors.isEmpty() || interceptors.stream().allMatch(i -> i.before(context, source));
    }

    /**
     * 之后
     *
     * @param context 上下文
     * @param target  目标
     * @return 是否继续
     */
    @Override
    public boolean after(C context, T target) {
        return interceptors.isEmpty() || interceptors.stream().allMatch(i -> i.after(context, target));
    }

    /**
     * 顺序
     *
     * @return 顺序
     */
    @Override
    public int order() {
        return order;
    }
}
