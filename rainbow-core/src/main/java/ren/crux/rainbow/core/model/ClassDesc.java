package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 属性描述
 *
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ClassDesc extends Describable {

    private List<FieldDesc> fields;

    private List<MethodDesc> methods;

}
