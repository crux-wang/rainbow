package ren.crux.rainbow.core.tuple;

import com.google.common.base.MoreObjects;
import lombok.Data;

/**
 * @author wangzhihui
 */
@Data
public class Describe implements Describable {

    protected String name;
    protected String qualifiedName;
    protected String description;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .omitNullValues()
                .toString();
    }
}
