package ren.crux.rainbow.javadoc.model;

import lombok.Data;

import java.util.List;

/**
 * 属性描述
 *
 * @author wangzhihui
 */
@Data
public class ClassDesc {
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
     * 属性描述列表
     */
    private List<FieldDesc> fields;
    /**
     * 方法描述列表
     */
    private List<MethodDesc> methods;

}
