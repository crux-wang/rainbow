package ren.crux.rainbow.javadoc.reader.parser.filter;

/**
 * 上下文配置器
 *
 * @author wangzhihui
 */
public interface ContextConfigurator {

    /**
     * 配置
     *
     * @param context 上下文
     */
    default void config(Context context) {
        context.addFilter(new DefaultClassDocFilter());
        context.addFilter(new DefaultMethodDocFilter());
    }
}
