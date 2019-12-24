package ren.crux.rainbow.core.parser;

import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.TagRef;
import ren.crux.rainbow.core.module.Context;

import java.util.Optional;

@Slf4j
public class CommentTextParser extends AbstractEnhanceParser<Doc, CommentText> {

    public static final CommentTextParser INSTANCE = new CommentTextParser();

    public CommentTextParser() {
    }

    public CommentTextParser(CombinationInterceptor<Doc, CommentText> combinationInterceptor) {
        super(combinationInterceptor);
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
            if (StringUtils.contains(source.text(), ".")) {
                try {
                    Class<?> aClass = Class.forName(source.text());
                    ref.setTarget(aClass.getTypeName());
                } catch (ClassNotFoundException ignored) {
                }
            }
            return Optional.of(ref);
        }
    }

}
