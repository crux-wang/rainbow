package ren.crux.rainbow.core.old.parser;

import com.sun.javadoc.FieldDoc;
import ren.crux.rainbow.core.model.Tuple;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.JavaDocParser;

public class FieldParser implements JavaDocParser<FieldDoc, Tuple> {

    @Override
    public boolean condition(Context context, FieldDoc source) {
        return source.isField();
    }

    @Override
    public Tuple parse(Context context, FieldDoc source) {
        Tuple tuple = new Tuple();
        tuple.setName(source.name());
        tuple.setQualifiedName(source.qualifiedName());
        tuple.setType(source.type().qualifiedTypeName());
        tuple.setDescription(source.getRawCommentText());
        tuple.setLink(context.parse(source.tags()));
        tuple.setInlineLink(context.parse(source.inlineTags()));
        return tuple;
    }
}
