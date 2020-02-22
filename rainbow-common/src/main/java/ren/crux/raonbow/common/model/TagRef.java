package ren.crux.raonbow.common.model;

import lombok.Data;

/**
 * 标签引用
 *
 * @author wangzhihui
 */
@Data
public class TagRef {

    /**
     * 标签内容
     */
    private String text;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 引用类型（ 全限定名 ）
     */
    private String target;

}
