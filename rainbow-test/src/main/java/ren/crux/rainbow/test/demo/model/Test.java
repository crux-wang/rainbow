package ren.crux.rainbow.test.demo.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Test
 * <p>
 * 测试专用
 * 包含内嵌链接 {@link User} 以及 linkplain 形式 {@linkplain Article article}
 *
 * @author wangzhihui
 * @since 1.0
 */
@Data
public class Test {
    /**
     * 字符串
     */
    private String strVal;
    /**
     * int
     */
    private int intVal;
    /**
     * Integer
     */
    private Integer integerVal;
    /**
     * long 数组
     */
    private long[] longArr;
    /**
     * int 数组
     */
    private Long[] integerArr;

    /**
     * String  二位数组
     */
    private String[][] strArr2;
    /**
     * long 二位数组
     */
    private long[][] longArr2;
    /**
     * String list
     */
    private List<String> list;
    /**
     * Test list
     */
    private List<Test> testList;
    /**
     * String-Test Map
     */
    private Map<String, Test> stringTestMap;
    /**
     * Test list list
     */
    private List<List<Test>> testList2;
    /**
     * String-Test List Map
     */
    private Map<String, List<Test>> stringTestListMap;

}
