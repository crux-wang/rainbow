package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.module.Context;
import ren.crux.raonbow.common.model.CommentText;
import ren.crux.raonbow.common.model.TagRef;

import java.util.Optional;

/**
 * 注释解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class CommentTextParser extends AbstractEnhanceParser<Doc, CommentText> {

    public CommentTextParser() {
    }

    public CommentTextParser(Interceptor<Doc, CommentText> interceptor) {
        super(interceptor);
    }

    private TagRefParser tagRefParser = new TagRefParser();

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<CommentText> parse0(Context context, Doc source) {
        if (source != null) {
            CommentText commentText = new CommentText();
            commentText.setText(source.commentText());
            commentText.setInlineTags(tagRefParser.parse(context, source.inlineTags()));
            commentText.setTags(tagRefParser.parse(context, source.tags()));
            return Optional.of(commentText);
        }
        return Optional.empty();
    }

    private static class TagRefParser implements Parser<Tag, TagRef> {

        /**
         * 解析
         *
         * @param context 上下文
         * @param source  源
         * @return 目标
         */
        @Override
        public Optional<TagRef> parse(Context context, Tag source) {
            if ("Text".equals(source.name())) {
                return Optional.empty();
            }
            TagRef ref = new TagRef();
            ref.setName(source.name());
            ref.setText(source.text());
            if (StringUtils.equalsAny(source.kind(), "@see", "@throws")) {
                context.getClassDocProvider().any().ifPresent(classDoc -> {
                    String className = source.text();
                    className = StringUtils.substringBefore(className, " ");
                    className = StringUtils.substringBefore(className, "#");
                    ClassDoc aClass = classDoc.findClass(className);
                    if (aClass != null) {
                        ref.setTarget(aClass.qualifiedTypeName());
                    }
                });
            }
            return Optional.of(ref);
        }
    }

}
