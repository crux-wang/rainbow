package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.EntryMethod;
import ren.crux.rainbow.core.model.TagRef;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.reflect.Method;
import java.util.Optional;

public class EntryMethodParser extends AbstractEnhanceParser<Method, EntryMethod> {

    private final AnnotationParser annotationParser;
    private final CommentTextParser commentTextParser;

    public EntryMethodParser(AnnotationParser annotationParser, CommentTextParser commentTextParser) {
        this.annotationParser = annotationParser;
        this.commentTextParser = commentTextParser;
    }

    public EntryMethodParser(CombinationInterceptor<Method, EntryMethod> combinationInterceptor, AnnotationParser annotationParser, CommentTextParser commentTextParser) {
        super(combinationInterceptor);
        this.annotationParser = annotationParser;
        this.commentTextParser = commentTextParser;
    }

    /**
     * 过滤
     *
     * @param source 源
     * @return 是否被过滤
     */
    @Override
    protected boolean filter(Method source) {
        return super.filter(source) && source.getParameters().length != 0;
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<EntryMethod> parse0(Context context, Method source) {
        String name = source.getName();
        EntryMethod entryMethod = new EntryMethod();
        entryMethod.setName(name);
        entryMethod.setAnnotations(annotationParser.parse(context, source.getAnnotations()));
        entryMethod.setReturnType(EntryUtils.build(source));
        context.addEntryClassName(entryMethod.getReturnType());
        Class<?> declaringClass = source.getDeclaringClass();
        context.getNoArgPublicMethodDoc(declaringClass, name).flatMap(methodDoc -> commentTextParser.parse(context, methodDoc)).ifPresent(entryMethod::setCommentText);
        CommentText commentText = entryMethod.getCommentText();
        if (commentText != null) {
            entryMethod.setReturnCommentText(commentText.getTagRef("@return").map(TagRef::getText).orElse(null));
        }
        return Optional.of(entryMethod);
    }
}
