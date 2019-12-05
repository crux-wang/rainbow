package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import ren.crux.rainbow.core.desc.model.CommentText;

import java.util.List;

/**
 * 请求参数
 *
 * @author wangzhihui
 */
@Data
public class RequestParam {
    /**
     * 请求参数类型
     */
    private RequestParamType paramType;
    /**
     * 参数类型
     */
    private String type;
    /**
     * 实际参数类型（对应泛型类型）
     */
    private String[] actualParamTypes;
    /**
     * 参数名
     */
    private String name;
    /**
     * 是否必须
     */
    private boolean required;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 注解列表
     */
    private List<AnnotationDesc> annotations;
    /**
     * 注释
     */
    private CommentText commentText;

}
