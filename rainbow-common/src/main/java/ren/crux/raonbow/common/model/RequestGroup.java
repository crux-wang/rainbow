package ren.crux.raonbow.common.model;

import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 请求组
 *
 * @author wangzhihui
 */
@Data
public class RequestGroup {
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
     * 路径
     */
    private String[] path;
    /**
     * 请求列表
     */
    private List<Request> requests;
    /**
     * 此请求组所用到的实体类型
     */
    private Set<String> entryClassNames = new HashSet<>();

    public void addRequest(@NonNull Request request) {
        if (requests == null) {
            requests = new LinkedList<>();
        }
        requests.add(request);
    }
}
