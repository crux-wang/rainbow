package ren.crux.rainbow.core.reader.parser;

import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Java 文档解析器
 *
 * @author wangzhihui
 */
public interface JavaDocParser<S, T> {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    boolean support(@NonNull Context context, @NonNull S source);

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    default Optional<T> parse(@NonNull Context context, @NonNull S source) {
        if (support(context, source)) {
            return parse0(context, source);
        } else {
            return Optional.empty();
        }
    }

    Optional<T> parse0(@NonNull Context context, @NonNull S source);

    /**
     * 批量解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    default List<T> parse(@NonNull Context context, @NonNull S[] source) {
        return Arrays.stream(source)
                .map(s -> parse(context, s))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
