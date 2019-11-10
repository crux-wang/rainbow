package ren.crux.rainbow.core.old.env;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.tuple.Describable;

/**
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Variable extends Describable {

    private String value;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .omitNullValues()
                .toString();
    }
}
