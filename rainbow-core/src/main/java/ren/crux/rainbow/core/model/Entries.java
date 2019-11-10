package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import ren.crux.rainbow.core.tuple.Mergeable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangzhihui
 */
public class Entries implements Mergeable<Entries> {

    private Map<String, Entry> entryMap = new HashMap<>();

    public void add(Entry entry) {
        entryMap.put(entry.getQualifiedName(), entry);
    }

    public Optional<Entry> get(String qualifiedName) {
        return Optional.ofNullable(entryMap.get(qualifiedName));
    }

    public Map<String, Entry> getEntryMap() {
        return entryMap;
    }

    @Override
    public void merge(Entries other) {
        entryMap.putAll(other.entryMap);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entryMap", entryMap)
                .omitNullValues()
                .toString();
    }
}
