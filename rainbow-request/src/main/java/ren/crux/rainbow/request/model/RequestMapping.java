package ren.crux.rainbow.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMapping {
    private String[] method;
    private String[] path;
}
