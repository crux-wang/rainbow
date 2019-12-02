package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 方法描述
 *
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MethodDesc extends Describable {

    private List<ParameterDesc> parameters;
    private Describable returnType;

}
