package ren.crux.rainbow.core.parser;

import java.util.Optional;

/**
 * 解析器
 *
 * @author wangzhihui
 */
public interface JavaDocParser<S, T> {

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    Optional<T> parse(Context context, S source);

}
