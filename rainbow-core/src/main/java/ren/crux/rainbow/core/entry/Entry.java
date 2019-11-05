package ren.crux.rainbow.core.entry;

import lombok.Data;

import java.util.List;

@Data
public class Entry {

    private String id;
    private String desc;
    private List<Field> fields;

}
