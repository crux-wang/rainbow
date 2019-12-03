package ren.crux.rainbow.test.demo.model;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * E1
 * <p>
 * xxx
 *
 * @author wangzhihui
 */
public class E1 {

    /**
     * string f1
     */
    private String f1;
    /**
     * <code>E2</code> e2 {@link String} and {@linkplain E2}
     * {@linkplain E2 e2asdas asdasd}
     *
     * @see E2
     */
    private E2 e2;

    /**
     * fun1
     *
     * @param param input param
     * @return empty string
     */
    public String fun1(String param) {
        return param;
    }


    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        E1 e1 = (E1) o;
        return f1.equals(e1.f1) &&
                e2.equals(e1.e2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(f1, e2);
    }

    public E2 getE2() {
        return e2;
    }

    public void setE2(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("f1", f1)
                .add("e2", e2)
                .omitNullValues()
                .toString();
    }
}
