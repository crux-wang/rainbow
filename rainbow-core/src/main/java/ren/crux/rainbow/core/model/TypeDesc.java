package ren.crux.rainbow.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDesc {

    private String type;
    private TypeDesc[] actualParamTypes;
    private String simpleName;
    private String name;
    private String format;

    public TypeDesc(String type, TypeDesc[] actualParamTypes) {
        this.type = type;
        this.actualParamTypes = actualParamTypes;
    }

    public TypeDesc(String type, TypeDesc[] actualParamTypes, String simpleName) {
        this.type = type;
        this.actualParamTypes = actualParamTypes;
        this.simpleName = simpleName;
    }

    public TypeDesc(String type, TypeDesc[] actualParamTypes, String simpleName, String name) {
        this.type = type;
        this.actualParamTypes = actualParamTypes;
        this.simpleName = simpleName;
        this.name = name;
    }

    public Type[] asOriginActualParamTypes() {
        if (actualParamTypes == null) {
            return null;
        }
        return Arrays.stream(actualParamTypes).map(TypeDesc::getType).map(className -> {
            try {
                return (Type) Class.forName(className);
            } catch (ClassNotFoundException ignored) {
                return null;
            }
        }).filter(Objects::nonNull).toArray(Type[]::new);
    }

    public String getFormat() {
        return StringUtils.defaultString(format, simpleName);
    }
}
