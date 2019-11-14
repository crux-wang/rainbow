package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.tuple.Describe;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Entry extends Describe {

    private List<Link> superLink = new LinkedList<>();
    private List<Field> field = new LinkedList<>();

    public void addSupperEntryLink(Link superLink) {
        if (this.superLink == null) {
            this.superLink = new LinkedList<>();
        }
        this.superLink.add(superLink);
    }

    public void addField(Field field) {
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
                .add("superLink", superLink)
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
