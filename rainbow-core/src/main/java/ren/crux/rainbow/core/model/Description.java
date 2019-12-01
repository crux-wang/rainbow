package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * 描述
 *
 * @author wangzhihui
 */
@Data
public class Description {
    /**
     * 注释
     */
    private String commentText;
    /**
     * 标签引用
     */
    private List<Ref> refs;
    /**
     * 注释中引用
     */
    private List<Ref> inlineRefs;

}
