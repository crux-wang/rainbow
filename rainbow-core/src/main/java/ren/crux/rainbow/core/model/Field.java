package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ren.crux.rainbow.core.tuple.Describe;

/**
 * @author wangzhihui
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Field extends Describe {

    private Describe type;

}
