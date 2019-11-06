package ren.crux.rainbow.core.entry;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.Describable;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tuple extends Describable {

    private String value;
    private String type;
    private String key;
    private String src;
    private List<Link> link;
    private List<Link> inlineLink;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("value", value)
                .add("type", type)
                .add("key", key)
                .add("src", src)
                .add("link", link)
                .add("inlineLink", inlineLink)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .omitNullValues()
                .toString();
    }
}
