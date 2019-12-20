package ren.crux.rainbow.core.model;

import lombok.Data;
import ren.crux.rainbow.javadoc.model.CommentText;

import java.util.List;

/**
 * 实体
 *
 * @author wangzhihui
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
     * 是否是接口
     */
    private boolean interfaceType;
    /**
     * 是否是枚举类
     */
    private boolean enumType;
    /**
     * 接口实现
     */
    private Entry impl;
    /**
     * 属性列表
     */
    private List<EntryField> fields;

}
