package ren.crux.rainbow.core.desc.reader.parser.filter;

public interface DocFilter<T> {

    default boolean doFilter(T doc) {
        return true;
    }

}
