package ren.crux.rainbow.request.model;

import lombok.Data;
import ren.crux.rainbow.entry.model.Annotation;

import java.util.List;

@Data
public class RequestParam {

    private String type;
    private String name;
    private boolean required;
    private Object defaultValue;
    private String group;
    private List<Annotation> annotations;

}
