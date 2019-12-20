package ren.crux.rainbow.core.model;

import lombok.Data;
import ren.crux.rainbow.javadoc.model.CommentText;

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
     * 注释
     */
    private CommentText commentText;
    /**
     * 属性列表
     */
    private List<EntryField> fields;

}
