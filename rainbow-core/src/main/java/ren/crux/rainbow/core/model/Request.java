package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 请求
 *
 * @author wangzhihui
 */
@Data
public class Request {

    /**
     * 描述
     */
    private Description desc;
    /**
     * 映射列表
     */
    private List<RequestMapping> requestMapping;
    /**
     * 参数列表
     */
    private List<RequestParam> params;
    /**
     * 注解列表
     */
    private List<Annotation> annotations;
    /**
     * 响应类型
     */
    private String response;
    /**
     * 响应类型参数（泛型）
     */
    private String[] responseParams;

}
