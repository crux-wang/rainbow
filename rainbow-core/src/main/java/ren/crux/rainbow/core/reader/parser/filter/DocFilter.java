package ren.crux.rainbow.core.reader.parser.filter;

public interface DocFilter<T> {

    default boolean doFilter(T doc) {
        return true;
    }

}
