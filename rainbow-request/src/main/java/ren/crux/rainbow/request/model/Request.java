package ren.crux.rainbow.request.model;

import lombok.Data;
import ren.crux.rainbow.entry.model.Annotation;
import ren.crux.rainbow.entry.model.Description;

import java.util.List;

@Data
public class Request {

    private Description desc;
    private List<RequestMapping> requestMapping;
    private String[] methods;
    private List<RequestParam> params;
    private String requestBodyType;
    private List<Annotation> annotations;

}
