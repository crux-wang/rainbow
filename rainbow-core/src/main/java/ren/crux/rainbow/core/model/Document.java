package ren.crux.rainbow.core.model;

import lombok.Data;
import ren.crux.rainbow.core.tuple.Mergeable;


/**
 * @author wangzhihui
 */
@Data
public class Document implements Mergeable<Document> {

    private Info info = new Info();
    private Requests requests = new Requests();
    private Entries entries = new Entries();

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public void addRequest(Request request) {
        this.requests.add(request);
    }

    @Override
    public void merge(Document other) {
        requests.merge(other.requests);
        entries.merge(other.entries);
    }

}
