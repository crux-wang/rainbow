package ren.crux.rainbow.core.model;

import lombok.Data;

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
    private Description desc;
    /**
     * 路径列表
     */
    private String[] path;
    /**
     * 请求列表
     */
    private List<Request> requests;

}
