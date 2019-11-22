package ren.crux.rainbow.entry.model;

import lombok.Data;

import java.util.List;

@Data
public class Entry {

    private String name;
    private Description desc;
    private List<Field> fields;
    private List<Annotation> annotations;

}
