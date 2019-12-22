package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 属性
 */
@Data
public class EntryField {
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private TypeDesc type;
    /**
     * 注释
     */
    private CommentText commentText;
    /**
     * 注解列表
     */
    private List<Annotation> annotations;

}
