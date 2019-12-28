package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.model.EntryField;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 实体属性解析器
 *
 * @author wangzhihui
 */
public class EntryFieldParser extends AbstractEnhanceParser<Field, EntryField> {

    private final AnnotationParser annotationParser;
    private final CommentTextParser commentTextParser;

    public EntryFieldParser(AnnotationParser annotationParser, CommentTextParser commentTextParser) {
        this.annotationParser = annotationParser;
        this.commentTextParser = commentTextParser;
    }

    public EntryFieldParser(Interceptor<Field, EntryField> interceptor, AnnotationParser annotationParser, CommentTextParser commentTextParser) {
        super(interceptor);
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
    protected Optional<EntryField> parse0(Context context, Field source) {
        Class<?> declaringClass = source.getDeclaringClass();
        EntryField entryField = new EntryField();
        entryField.setName(source.getName());
        entryField.setType(EntryUtils.build(source));
        context.addEntryClassName(entryField.getType());
        entryField.setAnnotations(annotationParser.parse(context, source.getAnnotations()));
        context.getClassFieldDoc(declaringClass, source.getName()).flatMap(fieldDoc -> commentTextParser.parse(context, fieldDoc)).ifPresent(entryField::setCommentText);
        return Optional.of(entryField);
    }
}
