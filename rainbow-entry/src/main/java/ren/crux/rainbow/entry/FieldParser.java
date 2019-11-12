package ren.crux.rainbow.entry;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;
import ren.crux.rainbow.core.model.Tuple;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.FieldDocParser;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public class FieldParser implements FieldDocParser {
    @Override
    public Optional<Tuple> parse(Context context, FieldDoc source) {
        Tuple tuple = new Tuple();
        tuple.setName(source.name());
        tuple.setQualifiedName(source.qualifiedName());
        tuple.setType(source.type().qualifiedTypeName());
        tuple.setTypeLink(context.getEntry(source.type().qualifiedTypeName()).map(context::getLink).orElse(null));
        tuple.setDescription(source.getRawCommentText());
        context.getTagDocParser().ifPresent(p -> {
            for (Tag tag : source.tags()) {
                p.parse(context, tag).ifPresent(tuple::addLink);
            }
            for (Tag tag : source.inlineTags()) {
                p.parse(context, tag).ifPresent(tuple::addInlineLink);
            }
        });
        return Optional.of(tuple);
    }
}
