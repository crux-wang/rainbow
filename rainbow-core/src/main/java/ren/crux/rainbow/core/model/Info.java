package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.Describable;

@EqualsAndHashCode(callSuper = true)
@Data
public class Info extends Describable {
    private String version;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("version", version)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .omitNullValues()
                .toString();
    }
}
