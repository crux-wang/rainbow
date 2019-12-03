package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import ren.crux.rainbow.core.desc.model.Describable;

import java.util.List;

@Data
public class Request {

    private Describable desc;
    private String path;
    private RequestMethod method;
    private List<RequestParam> params;

}
