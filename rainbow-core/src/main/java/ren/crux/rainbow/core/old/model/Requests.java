package ren.crux.rainbow.core.old.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.tuple.Mergeable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class Requests implements Mergeable<Requests> {

    private List<Request> requests = new LinkedList<>();

    public void addRequest(Request request) {
        requests.add(request);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("requests", requests)
                .toString();
    }

    @Override
    public void merge(Requests other) {
        requests.addAll(other.requests);
    }
}
