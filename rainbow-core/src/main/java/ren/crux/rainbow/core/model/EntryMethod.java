package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 实体方法 （无参方法）
 *
 * @author wangzhihui
 */
@Data
public class EntryMethod {
    /**
     * 名称
     */
    private String name;
    /**
     * 返回值类型
     */
    private TypeDesc returnType;
    /**
     * 注释
     */
    private CommentText commentText;
    /**
     * 返回值注释
     */
    private String returnCommentText;
    /**
     * 注解列表
     */
    private List<Annotation> annotations;

}
