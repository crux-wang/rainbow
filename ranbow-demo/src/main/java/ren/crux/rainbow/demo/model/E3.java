package ren.crux.rainbow.demo.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * E1
 * <p>
 * xxx
 *
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class E3 extends E2 {

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
