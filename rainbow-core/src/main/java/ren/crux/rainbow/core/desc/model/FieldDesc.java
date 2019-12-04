package ren.crux.rainbow.core.desc.model;

import lombok.Data;

/**
 * 属性描述
 *
 * @author wangzhihui
 */
@Data
public class FieldDesc {
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 注释
     */
    private CommentText commentText;
}
