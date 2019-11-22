package ren.crux.rainbow.entry.model;

import lombok.Data;

import java.util.List;

@Data
public class Field {

    private String name;
    private String type;
    private Description desc;
    private List<Constraint> constraints;
    private List<Annotation> annotations;
}
