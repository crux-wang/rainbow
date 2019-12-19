package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import ren.crux.rainbow.core.desc.model.CommentText;

import java.util.List;

/**
 * 实体
 */
@Data
public class Entry {
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 实际参数类型（对应泛型类型）
     */
    private String[] actualParamTypes;
    /**
     * 注释
     */
    private CommentText commentText;
    /**
     * 属性列表
     */
    private List<EntryField> fields;

}
