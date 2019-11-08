package ren.crux.rainbow.core.entry;

import com.google.common.base.MoreObjects;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangzhihui
 */
public class Entries {

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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("entryMap", entryMap)
                .omitNullValues()
                .toString();
    }
}
