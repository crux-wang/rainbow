package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.NonNull;
import ren.crux.rainbow.javadoc.model.CommentText;

import java.util.LinkedList;
import java.util.List;

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
     * 请求列表
     */
    private List<Request> requests;


    public void addRequest(@NonNull Request request) {
        if (requests == null) {
            requests = new LinkedList<>();
        }
        requests.add(request);
    }

}
