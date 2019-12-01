package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 属性
 *
 * @author wangzhihui
 */
@Data
public class Field {
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
     * 注解列表
     */
    private List<Annotation> annotations;

}
