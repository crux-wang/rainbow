package ren.crux.rainbow.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求映射
 *
 * @author wangzhihui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMapping {
    /**
     * 方法列表
     */
    private String[] method;
    /**
     * 路径列表
     */
    private String[] path;
}
