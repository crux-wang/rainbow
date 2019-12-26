package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 请求
 */
@Data
public class Request {
    /**
     * 名称 （方法名）
     */
    private String name;
    /**
     * 方法签名
     */
    private String signature;
    /**
     * 所在类类型
     */
    private String declaringType;
    /**
     * 注释
     */
    private CommentText commentText;
    /**
     * 路径列表
     */
    private String[] path;
    /**
     * 方法列表
     */
    private RequestMethod[] method;
    /**
     * 参数列表
     */
    private List<RequestParam> params;
    /**
     * 返回值类型
     */
    private TypeDesc returnType;
    /**
     * 返回值注释
     */
    private String returnCommentText;

}
