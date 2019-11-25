package ren.crux.rainbow.request.model;

import lombok.Data;
import ren.crux.rainbow.entry.model.Description;

import java.util.List;

@Data
public class RequestGroup {

    private String name;
    private Description desc;
    private String path;
    private List<Request> requests;

}
