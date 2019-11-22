package ren.crux.rainbow.entry.model;

import lombok.Data;

import java.util.List;

@Data
public class Description {

    private String commentText;
    private List<Ref> refs;
    private List<Ref> inlineRefs;

}
