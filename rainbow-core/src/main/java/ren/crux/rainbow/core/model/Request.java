package ren.crux.rainbow.core.model;

import lombok.Data;
import ren.crux.rainbow.javadoc.model.CommentText;

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
     * 类型 （方法全限定名）
     */
    private String type;
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
     * 返回值类型 （全限定名）
     */
    private TypeDesc returnType;
    /**
     * 返回值注释
     */
    private CommentText returnCommentText;

}
