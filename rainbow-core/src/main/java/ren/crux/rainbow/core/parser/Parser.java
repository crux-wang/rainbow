package ren.crux.rainbow.core.parser;

public interface Parser<S, T> {

    boolean condition(Context context, S source);

    T parse(Context context, S source);

}
