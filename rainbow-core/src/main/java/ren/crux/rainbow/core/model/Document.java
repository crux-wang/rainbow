package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.tuple.Mergeable;

import java.util.LinkedList;
import java.util.List;


/**
 * @author wangzhihui
 */
@Data
public class Document implements Mergeable<Document> {

    private Info info = new Info();
    private List<RequestItem> item = new LinkedList<>();
    private Entries entries = new Entries();

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void addItem(RequestItem item) {
        this.item.add(item);
    }

    public void addItem(Request request) {
        this.item.add(new RequestItem(request));
    }

    public void addItem(Requests requests) {
        for (Request request : requests.getRequests()) {
            this.item.add(new RequestItem(request));
        }
    }

    @Override
    public void merge(Document other) {
        item.addAll(other.item);
        entries.merge(other.entries);
    }

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
