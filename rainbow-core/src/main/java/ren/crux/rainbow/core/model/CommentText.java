package ren.crux.rainbow.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 注释
 *
 * @author wangzhihui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentText {

    /**
     * 注释内容
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

    public String content() {
        String tmp = StringUtils.trim(StringUtils.substringAfter(text, "\n"));
        tmp = StringUtils.removeStart(tmp, "<p>");
        tmp = StringUtils.removeStart(tmp, "\n");
        return StringUtils.trim(tmp);
    }

    public String firstLine() {
        return StringUtils.substringBefore(text, "\n");
    }

    public String inline() {
        return StringUtils.replaceEach(text, new String[]{"<p>", "\n"}, new String[]{"", ""});
    }

    public CommentText(String text) {
        this.text = text;
    }

    public Optional<TagRef> getTagRef(String name) {
        List<TagRef> tags = getTags();
        if (tags != null) {
            for (TagRef tag : tags) {
                if (StringUtils.equals(name, tag.getName())) {
                    return Optional.of(tag);
                }
            }
        }
        return Optional.empty();
    }

    public CommentText clearTags() {
        tags = null;
        return this;
    }
}
