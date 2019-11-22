package ren.crux.rainbow.entry.model;

import lombok.Data;

import java.util.Map;

/**
 * @author wangzhihui
 */
@Data
public class Annotation {

    private String name;
    private String type;
    private Map<String, Object> attribute;

}
