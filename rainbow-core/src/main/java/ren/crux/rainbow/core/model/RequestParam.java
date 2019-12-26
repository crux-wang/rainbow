package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 请求参数
 *
 * @author wangzhihui
 */
@Data
public class RequestParam {
    /**
     * 所在方法的签名
     */
    private String declaringSignature;
    /**
     * 请求参数类型
     */
    private RequestParamType paramType;
    /**
     * 参数类型
     */
    private TypeDesc type;
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
    private List<Annotation> annotations;
    /**
     * 注释
     */
    private CommentText commentText;

}
