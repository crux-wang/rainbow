package ren.crux.raonbow.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
     * 简称
     */
    private String simpleName;
    /**
     * 类型
     */
    private String type;
    /**
     * 注释
     */
    private CommentText commentText;
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
    private List<EntryField> fields = new LinkedList<>();
    /**
     * 方法列表
     */
    private List<EntryMethod> methods = new LinkedList<>();
    /**
     * 注解列表
     */
    private List<Annotation> annotations;
    /**
     * 额外字段
     */
    private Map<String, Object> extra = new HashMap<>();
}
