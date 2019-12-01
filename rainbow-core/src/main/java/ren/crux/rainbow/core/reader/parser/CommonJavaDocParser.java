package ren.crux.rainbow.core.reader.parser;

/**
 * 通用文档解析器
 *
 * @param <S>
 * @param <T>
 * @author wangzhihui
 */
public interface CommonJavaDocParser<S, T> extends JavaDocParser<S, T> {
    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    default boolean support(Context context, S source) {
        return true;
    }

}
