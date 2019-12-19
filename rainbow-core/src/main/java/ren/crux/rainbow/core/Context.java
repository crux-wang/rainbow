package ren.crux.rainbow.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Context {

    private final Map<String, Object> properties = new HashMap<>();

    private final Set<String> entryClassNames = new HashSet<>();

    public Map<String, Object> getProperties() {
        return properties;
    }
}
