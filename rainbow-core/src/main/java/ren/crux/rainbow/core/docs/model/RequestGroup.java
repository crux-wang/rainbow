package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import ren.crux.rainbow.core.desc.model.Describable;

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
     * 父路径列表
     */
    private String[] path;
    /**
     * 请求列表
     */
    private List<Request> requests;

}
