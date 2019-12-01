package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.Map;

/**
 * 注解
 *
 * @author wangzhihui
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

}
