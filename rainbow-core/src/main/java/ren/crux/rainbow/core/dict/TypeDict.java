package ren.crux.rainbow.core.dict;

import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.JSDataType;
import ren.crux.rainbow.core.model.TypeDesc;

import java.util.HashMap;
import java.util.Map;

public class TypeDict {

    private final Map<String, String> dict = new HashMap<>();

    public TypeDict useDefault() {
        dict.put("java.lang.String", JSDataType.String.name());
        dict.put("java.lang.Character", JSDataType.String.name());
        dict.put("char", JSDataType.String.name());
        dict.put("java.lang.Byte", JSDataType.String.name());
        dict.put("byte", JSDataType.String.name());
        dict.put("java.lang.Boolean", JSDataType.Boolean.name());
        dict.put("boolean", JSDataType.String.name());
        dict.put("java.lang.Integer", JSDataType.Number.name());
        dict.put("int", JSDataType.Number.name());
        dict.put("java.lang.Number", JSDataType.Number.name());
        dict.put("java.lang.Long", JSDataType.Number.name());
        dict.put("long", JSDataType.Number.name());
        dict.put("java.lang.Float", JSDataType.Number.name());
        dict.put("float", JSDataType.Number.name());
        dict.put("java.lang.Double", JSDataType.Number.name());
        dict.put("double", JSDataType.Number.name());
        dict.put("java.lang.Short", JSDataType.Number.name());
        dict.put("short", JSDataType.Number.name());
        dict.put("java.util.List", JSDataType.Array.name());
        dict.put("java.util.Map", JSDataType.Object.name());
        return this;
    }

    public TypeDict with(String source, String target) {
        dict.put(source, target);
        return this;
    }

    public TypeDict with(Map<String, String> dict) {
        this.dict.putAll(dict);
        return this;
    }

    public String translate(TypeDesc type) {
        return dict.getOrDefault(type.getType(), type.getSimpleName());
    }

    public String translate(String type) {
        return dict.getOrDefault(type, StringUtils.contains(type, ".") ? StringUtils.substringAfterLast(type, ".") : type);
    }
}
