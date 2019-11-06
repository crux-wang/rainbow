package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.Describable;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Item extends Describable {

    private List<Item> item;
    private Request request;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("item", item)
                .add("request", request)
                .add("name", name)
                .add("qualifiedName", qualifiedName)
                .add("description", description)
                .omitNullValues()
                .toString();
    }
}
