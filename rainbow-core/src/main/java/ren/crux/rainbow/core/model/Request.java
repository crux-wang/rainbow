package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class Request {

    private String uri;
    private List<String> method = new LinkedList<>();
    private Body body = new Body();
}
