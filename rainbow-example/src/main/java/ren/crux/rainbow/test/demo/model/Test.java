/*
 *  Copyright 2020. The Crux Authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ren.crux.rainbow.test.demo.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Test
 * <p>
 * 测试专用
 * 包含内嵌链接 {@link User} 以及 linkplain 形式 {@linkplain Article article}
 * {@link User#getEmail()} link 方法测试
 * {@link SubTest#pubField} link 属性测试
 *
 * @author wangzhihui
 * @see SubTest#exp()   @see 标签方法测试
 * @see SubTest#pubField  @see 标签属性测试
 * @see SubTest  @see 标签测试
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

    /**
     * 异常文档注释测试
     *
     * @throws Exception 异常
     */
    public void exp() throws Exception {

    }
}
