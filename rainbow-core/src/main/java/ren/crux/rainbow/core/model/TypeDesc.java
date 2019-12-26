package ren.crux.rainbow.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
