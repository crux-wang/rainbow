package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class RequestParam {

    /**
     * 请求参数类型
     */
    private RequestType requestType;
    /**
     * 参数类型
     */
    private String type;
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
    private Object defaultValue;
    /**
     * 注解列表
     */
    private List<Annotation> annotations;

}
