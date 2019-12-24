package ren.crux.rainbow.core.module;

import ren.crux.rainbow.core.builder.SubBuilder;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.interceptor.Interceptor;

public class InterceptorBuilder<S, T> extends SubBuilder<ModuleBuilder> {

    private CombinationInterceptor.CombinationInterceptorBuilder<S, T> builder = CombinationInterceptor.builder();

    public InterceptorBuilder(ModuleBuilder superBuilder) {
        super(superBuilder);
    }

    public InterceptorBuilder<S, T> interceptor(Interceptor<S, T> interceptor) {
        builder.interceptor(interceptor);
        return this;
    }

    public CombinationInterceptor<S, T> build() {
        return builder.build();
    }

}
