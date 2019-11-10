package ren.crux.rainbow.core.tuple;

/**
 * 可合并的
 *
 * @author wangzhihui
 */
public interface Mergeable<T> {

    /**
     * 合并
     *
     * @param other 另一个对象
     */
    void merge(T other);

}
