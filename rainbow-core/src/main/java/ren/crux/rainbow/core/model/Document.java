package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.entry.Entries;

import java.util.LinkedList;
import java.util.List;


/**
 * @author wangzhihui
 */
@Data
public class Document {

    private Info info = new Info();
    private List<Item> item = new LinkedList<>();
    private Entries entries = new Entries();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("info", info)
                .add("item", item)
                .add("entries", entries)
                .omitNullValues()
                .toString();
    }
}
