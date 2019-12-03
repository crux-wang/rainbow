package ren.crux.rainbow.core.desc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangzhihui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public CommentText(String text) {
        this.text = text;
    }
}
