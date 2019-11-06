package ren.crux.rainbow.core.entry;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.Describable;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Entry extends Describable {

    private Entry superEntry;
    private List<Tuple> field;
    private List<Link> link;
    private List<Link> inlineLink;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("superEntry", superEntry)
                .add("field", field)
                .add("link", link)
                .add("inlineLink", inlineLink)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .omitNullValues()
                .toString();
    }
}
