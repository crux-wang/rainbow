package ren.crux.rainbow.core;

import lombok.Data;

@Data
public abstract class Describable {

    protected String name;
    protected String qualifiedName;
    protected String description;

}
