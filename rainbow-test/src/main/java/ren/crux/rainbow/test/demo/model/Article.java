package ren.crux.rainbow.test.demo.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章
 */
@Data
public class Article {
    /**
     * 文章 ID
     */
    private String id;
    /**
     * 标题
     */
    @NotBlank
    @Min(5)
    @Max(15)
    private String title;
    /**
     * 标签列表
     */
    private List<String> tags;
    /**
     * 作者
     */
    private User author;
    /**
     * 内容
     */
    @NotNull
    private String content;
    /**
     * 状态
     */
    private ArticleStatus status;
    /**
     * 额外字段
     */
    private Map<String, Object> extra;
    /**
     * 更新时间
     */
    @FutureOrPresent
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;
}
