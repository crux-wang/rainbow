package ren.crux.rainbow.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeDesc {

    private String type;
    private String[] actualParamTypes;

    public String getName() {
        return StringUtils.substringAfterLast(type, ".");
    }

}
