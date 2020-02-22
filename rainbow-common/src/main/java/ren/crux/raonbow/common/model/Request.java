package ren.crux.raonbow.common.model;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    /**
     * 额外字段
     */
    private Map<String, Object> extra;


    public void putExtra(String key, Object value) {
        if (extra == null) {
            extra = new HashMap<>();
        }
        extra.put(key, value);
    }

    public Optional<Object> getExtra(String key) {
        if (extra == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(extra.get(key));
    }
}
