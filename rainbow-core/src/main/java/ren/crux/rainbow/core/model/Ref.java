package ren.crux.rainbow.core.model;

import lombok.Data;

/**
 * 引用
 *
 * @author wangzhihui
 */
@Data
public class Ref {
    /**
     * 文本
     */
    private String text;
    /**
     * 名称
     */
    private String name;
    /**
     * 目标
     */
    private String target;

}
