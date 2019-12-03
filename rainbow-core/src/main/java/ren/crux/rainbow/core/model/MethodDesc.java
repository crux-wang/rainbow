package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 方法描述
 *
 * @author wangzhihui
 */
@Data
public class MethodDesc {
    private Describable desc;
    private List<ParameterDesc> parameters;
    private Describable returnType;

}
