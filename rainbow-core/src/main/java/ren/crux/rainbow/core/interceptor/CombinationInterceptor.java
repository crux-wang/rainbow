package ren.crux.rainbow.core.interceptor;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import ren.crux.rainbow.core.module.Context;

import java.util.List;
import java.util.function.Consumer;

/**
 * 组合解析器
 *
 * @param <S> 源
 * @param <T> 目标
 * @author wangzhihui
 */
@Builder(toBuilder = true)
public class CombinationInterceptor<S, T> implements Interceptor<S, T> {

    /**
     * 顺序
     */
    protected final int order;
    /**
     * 拦截器列表
     */
    @Singular
    protected final List<Interceptor<S, T>> interceptors;

    public CombinationInterceptor(@NonNull List<Interceptor<S, T>> interceptors) {
        this(0, interceptors);
    }

    public CombinationInterceptor(int order, @NonNull List<Interceptor<S, T>> interceptors) {
        this.order = order;
        this.interceptors = interceptors;
    }

    public boolean isEmpty() {
        return interceptors.isEmpty();
    }

    /**
     * 之前
     *
     * @param context 上下文
     * @param source  源
     * @return 是否继续
     */
    @Override
    public boolean before(Context context, S source) {
        return interceptors.isEmpty() || interceptors.stream().allMatch(i -> i.before(context, source));
    }

    /**
     * 之后
     *
     * @param context 上下文
     * @param target  目标
     * @param source  源
     * @return 是否继续
     */
    @Override
    public boolean after(Context context, S source, T target) {
        return interceptors.isEmpty() || interceptors.stream().allMatch(i -> i.after(context, source, target));
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

    public void ifPresent(Consumer<CombinationInterceptor<S, T>> consumer) {
        if (!isEmpty()) {
            consumer.accept(this);
        }
    }
}
