package ren.crux.rainbow.entry;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;
import ren.crux.rainbow.core.model.Tuple;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.FieldDocParser;

/**
 * @author wangzhihui
 */
public class FieldParser implements FieldDocParser {
    @Override
    public Tuple parse(Context context, FieldDoc source) {
        Tuple tuple = new Tuple();
        tuple.setName(source.name());
        tuple.setQualifiedName(source.qualifiedName());
        tuple.setType(source.type().qualifiedTypeName());
        tuple.setTypeLink(context.getEntry(source.type().qualifiedTypeName()).map(context::getLink).orElse(null));
        tuple.setDescription(source.getRawCommentText());
        context.getTagDocParser().ifPresent(p -> {
            for (Tag tag : source.tags()) {
                tuple.addLink(p.parse(context, tag));
            }
            for (Tag tag : source.inlineTags()) {
                tuple.addInlineLink(p.parse(context, tag));
            }
        });
        return tuple;
    }
}
