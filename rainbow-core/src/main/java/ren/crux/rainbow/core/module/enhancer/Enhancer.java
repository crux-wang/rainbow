package ren.crux.rainbow.core.module.enhancer;

import ren.crux.rainbow.core.Context;

public interface Enhancer<T> {

    void enhance(Context context, T t);

}
