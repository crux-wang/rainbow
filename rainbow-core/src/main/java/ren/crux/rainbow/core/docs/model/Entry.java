package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import ren.crux.rainbow.core.desc.model.Describable;

import java.util.List;

@Data
public class Entry {

    private Describable desc;
    private List<FieldDetail> fields;

}
