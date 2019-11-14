package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ren.crux.rainbow.core.tuple.Describe;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author wangzhihui
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Info extends Describe {

    private List<String> packages = new LinkedList<>();
    private Map<String, String> version;

}
