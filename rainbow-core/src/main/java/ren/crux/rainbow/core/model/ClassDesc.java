package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 属性描述
 *
 * @author wangzhihui
 */
@Data
public class ClassDesc {

    private Describable desc;
    private List<FieldDesc> fields;
    private List<MethodDesc> methods;

}
