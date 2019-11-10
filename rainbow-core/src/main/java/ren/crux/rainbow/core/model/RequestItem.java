package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ren.crux.rainbow.core.tuple.Describe;

import java.util.List;

/**
 * @author wangzhihui
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestItem extends Describe {

    private Request request;
    private List<RequestItem> item;

    public RequestItem(Request request) {
        this.request = request;
    }

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
