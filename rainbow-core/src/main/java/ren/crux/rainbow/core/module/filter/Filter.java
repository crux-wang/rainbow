package ren.crux.rainbow.core.module.filter;

import ren.crux.rainbow.core.Context;

public interface Filter<T> {

    boolean doFilter(Context context, T t);

}
