package ren.crux.rainbow.core.docs.model;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Document {
    private List<RequestGroup> requestGroups;
    private Map<String, Entry> entryMap;

    public void addRequestGroup(RequestGroup requestGroup) {
        if (requestGroups == null) {
            requestGroups = new LinkedList<>();
        }
        requestGroups.add(requestGroup);
    }

    public void addEntry(Entry entry) {
        if (entryMap == null) {
            entryMap = new HashMap<>(16);
        }
        entryMap.put(entry.getDesc().getType(), entry);
    }
}
