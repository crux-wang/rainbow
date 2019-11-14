package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.FieldDoc;
import ren.crux.rainbow.core.model.Field;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.FieldDocParser;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public class FieldParser implements FieldDocParser {

    @Override
    public boolean support(Context context, FieldDoc source) {
        return source.isField();
    }

    @Override
    public Optional<Field> parse(Context context, FieldDoc source) {
        Field tuple = new Field();
        tuple.setName(source.name());
        tuple.setQualifiedName(source.qualifiedName());
        tuple.setType(source.type().qualifiedTypeName());
        tuple.setTypeLink(context.getEntry(source.type().qualifiedTypeName()).map(context::getLink).orElse(null));
        tuple.setDescription(source.getRawCommentText());
//        context.getTagDocParser().ifPresent(p -> {
//            for (Tag tag : source.tags()) {
//                p.parse(context, tag).ifPresent(tuple::addLink);
//            }
//            for (Tag tag : source.inlineTags()) {
//                p.parse(context, tag).ifPresent(tuple::addInlineLink);
//            }
//        });
        return Optional.of(tuple);
    }
}
