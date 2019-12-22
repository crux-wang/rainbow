package ren.crux.rainbow.common;

import java.util.List;
import java.util.Optional;

/**
 * 解析器
 *
 * @param <S> 源
 * @param <T> 目标
 * @param <C> 上下文
 * @author wangzhihui
 */
public interface Parser<S, T, C> {
    /**
     * 解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    Optional<T> parse(C context, S source);

    /**
     * 批量解析
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    List<T> parse(C context, S[] source);

}
