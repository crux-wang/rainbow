package ren.crux.rainbow.javadoc.reader.impl;

import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import ren.crux.rainbow.javadoc.model.CommentText;
import ren.crux.rainbow.javadoc.model.FieldDesc;
import ren.crux.rainbow.javadoc.reader.parser.CommonDocParser;
import ren.crux.rainbow.javadoc.reader.parser.Context;
import ren.crux.rainbow.javadoc.reader.parser.FieldDocParser;

import java.util.Optional;

public class FieldDescParser implements FieldDocParser<FieldDesc> {

    public static final FieldDescParser INSTANCE = new FieldDescParser();

    private final CommonDocParser<CommentText> descriptionDocParser = CommentTextParser.INSTANCE;

    @Override
    public Optional<FieldDesc> parse(@NonNull Context context, @NonNull FieldDoc source) {
        if (context.doFilter(source)) {
            FieldDesc fieldDesc = new FieldDesc();
            fieldDesc.setName(source.name());
            fieldDesc.setType(source.type().qualifiedTypeName());
            descriptionDocParser.parse(context, source).ifPresent(fieldDesc::setCommentText);
            return Optional.of(fieldDesc);
        }
        return Optional.empty();
    }
}
