package ren.crux.rainbow.core.reader.impl;

import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.model.Describable;
import ren.crux.rainbow.core.model.FieldDesc;
import ren.crux.rainbow.core.reader.parser.CommonDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.reader.parser.FieldDocParser;

import java.util.Optional;

public class FieldDescParser implements FieldDocParser<FieldDesc> {

    public static final FieldDescParser INSTANCE = new FieldDescParser();

    private final CommonDocParser<Describable> descriptionDocParser = DescriptionDocParser.INSTANCE;

    @Override
    public Optional<FieldDesc> parse(@NonNull Context context, @NonNull FieldDoc source) {
        if (context.getFieldDocFilter().doFilter(source)) {
            return descriptionDocParser.parse(context, source).map(desc -> {
                FieldDesc fieldDesc = new FieldDesc();
                fieldDesc.setDesc(desc);
                return fieldDesc;
            });
        }
        return Optional.empty();
    }
}
