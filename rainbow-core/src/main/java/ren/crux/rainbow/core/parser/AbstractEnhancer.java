package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.CombinationInterceptor;

public abstract class AbstractEnhancer<T> extends AbstractEnhanceParser<T, T> {

    public AbstractEnhancer() {
    }

    public AbstractEnhancer(CombinationInterceptor<T, T> combinationInterceptor) {
        super(combinationInterceptor);
    }
}
