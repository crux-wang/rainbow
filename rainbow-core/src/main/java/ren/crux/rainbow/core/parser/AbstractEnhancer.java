package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.Interceptor;

/**
 * 抽象增强器
 *
 * @author wangzhihui
 */
public abstract class AbstractEnhancer<T> extends AbstractEnhanceParser<T, T> {

    public AbstractEnhancer() {
    }

    public AbstractEnhancer(Interceptor<T, T> interceptor) {
        super(interceptor);
    }
}
