package ren.crux.rainbow.request.model;

import lombok.Data;
import ren.crux.rainbow.entry.model.Entry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class RequestGroups {
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
            entryMap = new HashMap<>();
        }
        entryMap.put(entry.getName(), entry);
    }
}
