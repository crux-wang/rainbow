package ren.crux.rainbow.entry.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Constraint {

    private String group;
    private String name;
    private Map<String, Object> extra = new HashMap<>();
}
