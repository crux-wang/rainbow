package ren.crux.rainbow.test.demo.model;

import lombok.Data;

/**
 * E1
 * <p>
 * xxx
 *
 * @author wangzhihui
 */
@Data
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

}
