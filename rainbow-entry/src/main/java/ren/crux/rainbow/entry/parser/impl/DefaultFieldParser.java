package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.model.Field;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.FieldParser;

import java.util.Optional;

public class DefaultFieldParser implements FieldParser {

    private final DescriptionDocParser descriptionDocParser;
    private final AnnotationDocParser annotationParser;

    public DefaultFieldParser(DescriptionDocParser descriptionDocParser, AnnotationDocParser annotationParser) {
        this.descriptionDocParser = descriptionDocParser;
        this.annotationParser = annotationParser;
    }

    public DefaultFieldParser() {
        this.descriptionDocParser = new DescriptionParser();
        this.annotationParser = new AnnotationParser();
    }

    @Override
    public boolean support(@NonNull Context context, @NonNull FieldDoc source) {
        return true;
    }

    @Override
    public Optional<Field> parse(@NonNull Context context, @NonNull FieldDoc source) {
        Field field = new Field();
        field.setName(source.name());
        field.setType(source.type().qualifiedTypeName());
        descriptionDocParser.parse(context, source).ifPresent(field::setDesc);
        field.setAnnotations(annotationParser.parse(context, source.annotations()));
        return Optional.of(field);
    }
}
