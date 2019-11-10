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
@Data
@EqualsAndHashCode(callSuper = true)
public class Tuple extends Describe {

    private String value;
    private String type;
    private Link typeLink;
    private String key;
    private String src;
    private List<Link> link;
    private List<Link> inlineLink;

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
                .add("value", value)
                .add("type", type)
                .add("typeLink", typeLink)
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
