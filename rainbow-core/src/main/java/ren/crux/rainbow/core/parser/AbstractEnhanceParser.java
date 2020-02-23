package ren.crux.rainbow.core.parser;

import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.module.Context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 抽象增强解析器
 *
 * @param <S> 源
 * @param <T> 目标
 * @author wangzhihui
 */
public abstract class AbstractEnhanceParser<S, T> implements Parser<S, T> {

    /**
     * 拦截器
     */
    protected final Interceptor<S, T> interceptor;

    public AbstractEnhanceParser() {
        this(null);
    }

    public AbstractEnhanceParser(Interceptor<S, T> interceptor) {
        this.interceptor = interceptor;
    }

    /**
     * 过滤
     *
     * @param source 源
     * @return 是否被过滤
     */
    protected boolean filter(S source) {
        return source != null;
    }

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    public Optional<T> parse(Context context, S source) {
        if (filter(source)) {
            if (interceptor == null) {
                return parse0(context, source);
            }
            if (interceptor.before(context, source)) {
                Optional<T> optional = parse0(context, source);
                if (optional.isPresent()) {
                    if (interceptor.after(context, source, optional.get())) {
                        return optional;
                    }
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 批量解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    public List<T> parse(Context context, S[] source) {
        if (ArrayUtils.isNotEmpty(source)) {
            if (source.length == 1) {
                return parse(context, source[0]).map(Collections::singletonList).orElse(Collections.emptyList());
            }
            return Arrays.stream(source).map(s -> parse(context, s).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    protected abstract Optional<T> parse0(Context context, S source);

}
