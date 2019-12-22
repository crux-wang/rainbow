package ren.crux.rainbow.common;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象增强解析器
 *
 * @param <S> 源
 * @param <T> 目标
 * @param <C> 上下文
 * @author wangzhihui
 */
public abstract class AbstractEnhanceParser<S, T, C> implements Parser<S, T, C> {

    /**
     * 组合拦截器
     */
    protected final CombinationInterceptor<S, T, C> combinationInterceptor;

    protected AbstractEnhanceParser() {
        this(new CombinationInterceptor<>(Collections.emptyList()));
    }

    protected AbstractEnhanceParser(CombinationInterceptor<S, T, C> combinationInterceptor) {
        this.combinationInterceptor = combinationInterceptor;
    }

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    public final List<T> parse(C context, S[] source) {
        if (ArrayUtils.isNotEmpty(source)) {
            if (source.length == 1) {
                return parse(context, source[0]).map(Collections::singletonList).orElse(Collections.emptyList());
            }
            return Arrays.stream(source).map(s -> parse(context, s).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 批量解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    public final Optional<T> parse(C context, S source) {
        if (combinationInterceptor.before(context, source)) {
            Optional<T> optional = parse0(context, source);
            if (optional.isPresent()) {
                if (combinationInterceptor.after(context, optional.get())) {
                    return optional;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    protected abstract Optional<T> parse0(C context, S source);

}
