package ren.crux.rainbow.core.model;

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

    public void add(Request request) {
        requests.add(request);
    }

    @Override
    public void merge(Requests other) {
        requests.addAll(other.requests);
    }
}
