package ren.crux.rainbow.core.docs.model;

import lombok.Data;

import java.util.Map;

/**
 * 注解描述
 */
@Data
public class Annotation {

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 属性列表
     */
    private Map<String, Object> attribute;
    /**
     * 描述
     */
    private String text;

}
