package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.desc.model.CommentText;
import ren.crux.rainbow.core.desc.model.FieldDesc;
import ren.crux.rainbow.core.desc.reader.parser.CommonDocParser;
import ren.crux.rainbow.core.desc.reader.parser.Context;
import ren.crux.rainbow.core.desc.reader.parser.FieldDocParser;

import java.util.Optional;

public class FieldDescParser implements FieldDocParser<FieldDesc> {

    public static final FieldDescParser INSTANCE = new FieldDescParser();

    private final CommonDocParser<CommentText> descriptionDocParser = CommentTextParser.INSTANCE;

    @Override
    public Optional<FieldDesc> parse(@NonNull Context context, @NonNull FieldDoc source) {
        if (context.getFieldDocFilter().doFilter(source)) {
            return descriptionDocParser.parse(context, source).map(commentText -> {
                FieldDesc fieldDesc = new FieldDesc();
                fieldDesc.setName(source.qualifiedName());
                fieldDesc.setType(source.type().qualifiedTypeName());
                fieldDesc.setCommentText(commentText);
                return fieldDesc;
            });
        }
        return Optional.empty();
    }
}
