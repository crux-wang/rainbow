package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import lombok.NonNull;
import ren.crux.rainbow.core.desc.model.Describable;

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
     * 描述
     */
    private Describable desc;
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
