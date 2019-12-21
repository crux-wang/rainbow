package ren.crux.rainbow.core;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.TypeDesc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
            if (typeDesc.getActualParamTypes() != null) {
                for (TypeDesc actualParamType : typeDesc.getActualParamTypes()) {
                    addEntryClassName(actualParamType);
                }
            }
        }
    }
}
