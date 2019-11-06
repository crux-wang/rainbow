package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;

import java.util.List;


@Data
public class Document {
    private Info info;
    private List<Item> item;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("info", info)
                .add("item", item)
                .omitNullValues()
                .toString();
    }
}
