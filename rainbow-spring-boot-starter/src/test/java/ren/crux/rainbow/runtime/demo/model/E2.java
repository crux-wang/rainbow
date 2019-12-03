package ren.crux.rainbow.runtime.demo.model;

import lombok.Data;

/**
 * E1
 * <p>
 * xxx
 *
 * @author wangzhihui
 */
@Data
public class E2 {

    /**
     * string f1
     */
    private String f1;

    /**
     * fun1
     *
     * @return empty string
     */
    private String fun1() {
        return "";
    }

}
