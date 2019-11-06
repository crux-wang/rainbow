package ren.crux.rainbow.core.entry;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.Describable;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Entry extends Describable {

    private List<Link> superEntryRef;
    private List<Tuple> field;
    private List<Link> link;
    private List<Link> inlineLink;

    public void addSupperEntryRef(Link superEntryRef) {
        if (this.superEntryRef == null) {
            this.superEntryRef = new LinkedList<>();
        }
        this.superEntryRef.add(superEntryRef);
    }

    public void addField(Tuple field) {
        if (this.field == null) {
            this.field = new LinkedList<>();
        }
        this.field.add(field);
    }

    public void addLink(Link link) {
        if (this.link == null) {
            this.link = new LinkedList<>();
        }
        this.link.add(link);
    }

    public void addInlineLink(Link inlineLink) {
        if (this.inlineLink == null) {
            this.inlineLink = new LinkedList<>();
        }
        this.inlineLink.add(inlineLink);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("superEntryRef", superEntryRef)
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
