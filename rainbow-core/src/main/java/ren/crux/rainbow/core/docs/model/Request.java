package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import ren.crux.rainbow.core.desc.model.Describable;

import java.util.List;

@Data
public class Request {

    private Describable desc;
    private String[] path;
    private RequestMethod[] method;
    private List<RequestParam> params;
    private String returnType;
    /**
     * 实际参数类型（对应泛型类型）
     */
    private String[] actualParamTypes;

}
