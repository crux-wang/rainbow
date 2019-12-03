package ren.crux.rainbow.core.docs.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ren.crux.rainbow.core.desc.model.FieldDesc;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FieldDetail extends FieldDesc {

    private List<Constraint> annotations;

}
