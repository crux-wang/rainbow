package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.tuple.Describe;

/**
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Info extends Describe {

    private String version;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .add("version", version)
                .omitNullValues()
                .toString();
    }
}
