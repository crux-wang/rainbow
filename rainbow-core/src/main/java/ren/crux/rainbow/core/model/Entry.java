package ren.crux.rainbow.core.model;

import lombok.Data;

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
     * 描述
     */
    private Description desc;
    /**
     * 属性列表
     */
    private List<Field> fields;
    /**
     * 注解列表
     */
    private List<Annotation> annotations;

}
