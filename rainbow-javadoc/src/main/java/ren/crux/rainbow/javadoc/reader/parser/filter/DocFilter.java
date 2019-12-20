package ren.crux.rainbow.javadoc.reader.parser.filter;

public interface DocFilter<T> {

    default boolean doFilter(T doc) {
        return true;
    }

}
