package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class CommentText {

    /**
     * 注释
     */
    private String text;
    /**
     * 标签引用
     */
    private List<TagRef> tags;
    /**
     * 注释中引用
     */
    private List<TagRef> inlineTags;

}
