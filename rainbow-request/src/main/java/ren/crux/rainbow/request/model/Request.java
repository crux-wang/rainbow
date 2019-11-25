package ren.crux.rainbow.request.model;

import lombok.Data;

import java.util.List;

@Data
public class Request {

    private String desc;
    private String path;
    private String[] methods;
    private List<RequestParam> requestParams;
    private List<RequestParam> PathVariables;
    private List<RequestParam> requestHeaders;
    private List<RequestParam> requestAttributes;
    private String requestBodyType;


}
