package ren.crux.rainbow.core.parser;

import com.sun.javadoc.FieldDoc;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.model.EntryField;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.reflect.Field;
import java.util.Optional;

public class EntryFieldParser extends AbstractEnhanceParser<Pair<Field, FieldDoc>, EntryField> {

    private final AnnotationParser annotationParser;
    private final CommentTextParser commentTextParser;

    public EntryFieldParser(AnnotationParser annotationParser, CommentTextParser commentTextParser) {
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
    protected Optional<EntryField> parse0(Context context, Pair<Field, FieldDoc> source) {
        if (source == null || source.getLeft() == null) {
            return Optional.empty();
        }
        Field field = source.getLeft();
        FieldDoc fieldDoc = source.getRight();
        EntryField entryField = new EntryField();
        entryField.setName(field.getName());
        entryField.setType(EntryUtils.build(field));
        entryField.setAnnotations(annotationParser.parse(context, field.getAnnotations()));
        commentTextParser.parse(context, fieldDoc).ifPresent(entryField::setCommentText);
        return Optional.of(entryField);
    }
}
