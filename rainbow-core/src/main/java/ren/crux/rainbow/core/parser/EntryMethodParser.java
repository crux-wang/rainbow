package ren.crux.rainbow.core.parser;

import com.sun.javadoc.MethodDoc;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.EntryMethod;
import ren.crux.rainbow.core.model.TagRef;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.reflect.Method;
import java.util.Optional;

public class EntryMethodParser extends AbstractEnhanceParser<Pair<Method, MethodDoc>, EntryMethod> {

    private final AnnotationParser annotationParser;
    private final CommentTextParser commentTextParser;

    public EntryMethodParser(AnnotationParser annotationParser, CommentTextParser commentTextParser) {
        this.annotationParser = annotationParser;
        this.commentTextParser = commentTextParser;
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<EntryMethod> parse0(Context context, Pair<Method, MethodDoc> source) {
        if (source == null || source.getLeft() == null) {
            return Optional.empty();
        }
        Method method = source.getLeft();
        if (method.getParameters().length != 0) {
            return Optional.empty();
        }
        MethodDoc methodDoc = source.getRight();
        String name = method.getName();
        EntryMethod entryMethod = new EntryMethod();
        entryMethod.setName(name);
        entryMethod.setAnnotations(annotationParser.parse(context, method.getAnnotations()));
        entryMethod.setReturnType(EntryUtils.build(method));
        commentTextParser.parse(context, methodDoc).ifPresent(entryMethod::setCommentText);
        CommentText commentText = entryMethod.getCommentText();
        if (commentText != null) {
            entryMethod.setReturnCommentText(commentText.getTagRef("@return").map(TagRef::getText).orElse(null));
        }
        return Optional.of(entryMethod);
    }
}
