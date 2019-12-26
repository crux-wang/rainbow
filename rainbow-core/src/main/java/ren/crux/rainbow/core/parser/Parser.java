package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.module.Context;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 解析器
 *
 * @param <S> 源
 * @param <T> 目标
 * @author wangzhihui
 */
public interface Parser<S, T> {
    /**
     * 解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    Optional<T> parse(Context context, S source);

    /**
     * 批量解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    default List<T> parse(Context context, S[] source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(source).map(s -> parse(context, s)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    /**
     * 批量解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    default List<T> parse(Context context, Collection<S> source) {
        if (source == null) {
            return Collections.emptyList();
        }
        return source.stream().map(s -> parse(context, s)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

}
