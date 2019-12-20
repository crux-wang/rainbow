package ren.crux.rainbow.core;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.TypeDesc;

import java.util.*;

@Getter
public class Context {

    private final Map<String, Object> properties = new HashMap<>();

    private final Set<String> entryClassNames = new HashSet<>();

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public void addEntryClassName(String className) {
        if (StringUtils.isNotBlank(className)) {
            entryClassNames.add(className);
        }
    }

    public void addEntryClassName(TypeDesc typeDesc) {
        if (typeDesc != null) {
            addEntryClassName(typeDesc.getType());
            addEntryClassName(typeDesc.getActualParamTypes());
        }
    }

    public void addEntryClassName(String[] classNames) {
        if (classNames != null) {
            entryClassNames.addAll(Arrays.asList(classNames));
        }
    }
}
