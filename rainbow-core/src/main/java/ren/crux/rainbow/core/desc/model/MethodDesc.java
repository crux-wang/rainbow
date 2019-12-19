package ren.crux.rainbow.core.desc.model;

import lombok.Data;

import java.util.List;

/**
 * 方法描述
 *
 * @author wangzhihui
 */
@Data
public class MethodDesc {
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
     * 参数描述列表
     */
    private List<ParameterDesc> parameters;
    /**
     * 返回值类型描述
     */
    private ClassDesc returnType;

}
