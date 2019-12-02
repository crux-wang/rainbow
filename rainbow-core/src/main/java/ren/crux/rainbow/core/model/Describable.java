package ren.crux.rainbow.core.model;

import lombok.Data;

/**
 * @author wangzhihui
 */
@Data
public class Describable {

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 注解文字
     */
    private CommentText commentText;

}
