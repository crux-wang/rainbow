package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.tuple.Describe;

import java.util.List;

/**
 * @author wangzhihui
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tuple extends Describe {

    private String value;
    private Link type;
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
