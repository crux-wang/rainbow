package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.utils.DocHelper;
import ren.crux.rainbow.entry.model.Entry;
import ren.crux.rainbow.entry.model.Field;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.EntryDocParser;
import ren.crux.rainbow.entry.parser.FieldParser;

import java.util.List;
import java.util.Optional;

public class EntryParser implements EntryDocParser {

    private final FieldParser fieldParser;
    private final AnnotationDocParser annotationParser;
    private final DescriptionDocParser descriptionDocParser;


    public EntryParser(FieldParser fieldParser, AnnotationDocParser annotationParser, DescriptionDocParser descriptionDocParser) {
        this.fieldParser = fieldParser;
        this.annotationParser = annotationParser;
        this.descriptionDocParser = descriptionDocParser;
    }

    public EntryParser() {
        this.fieldParser = new DefaultFieldParser();
        this.annotationParser = new AnnotationParser();
        this.descriptionDocParser = new DescriptionParser();
    }

    @Override
    public boolean support(@NonNull Context context, @NonNull ClassDoc source) {
        return DocHelper.hasAnyFieldDoc(source);
    }

    @Override
    public Optional<Entry> parse(@NonNull Context context, @NonNull ClassDoc source) {
        System.out.println("parse : " + source.name());
        List<FieldDoc> fieldDocs = DocHelper.getAllFieldDoc(source);
        List<Field> fields = fieldParser.parse(context, fieldDocs.toArray(new FieldDoc[0]));
        if (!fields.isEmpty()) {
            Entry entry = new Entry();
            entry.setName(source.name());
            descriptionDocParser.parse(context, source).ifPresent(entry::setDesc);
            entry.setFields(fields);
            entry.setAnnotations(annotationParser.parse(context, source.annotations()));
            return Optional.of(entry);
        }
        return Optional.empty();
    }

}
