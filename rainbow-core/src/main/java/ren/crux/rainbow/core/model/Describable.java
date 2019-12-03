package ren.crux.rainbow.core.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author wangzhihui
 */
@Data
public class Describable {

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 注解文字
     */
    private CommentText commentText;


    public Optional<TagRef> getTagRef(String name) {
        if (commentText != null) {
            List<TagRef> tags = commentText.getTags();
            if (tags != null) {
                for (TagRef tag : tags) {
                    if (StringUtils.equals(name, tag.getName())) {
                        return Optional.of(tag);
                    }
                }
            }
        }
        return Optional.empty();
    }

}
