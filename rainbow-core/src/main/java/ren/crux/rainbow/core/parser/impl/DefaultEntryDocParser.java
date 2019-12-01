package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.Field;
import ren.crux.rainbow.core.parser.EntryDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.utils.DocHelper;

import java.util.List;
import java.util.Optional;

/**
 * 默认实体文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class DefaultEntryDocParser implements EntryDocParser {

    @Override
    public Optional<Entry> parse(@NonNull Context context, @NonNull ClassDoc source) {
        log.info("parse entry : {}", source.name());
        List<FieldDoc> fieldDocs = DocHelper.getAllFieldDoc(source);
        List<Field> fields = context.getEntryFieldDocParser().parse(context, fieldDocs.toArray(new FieldDoc[0]));
        if (!fields.isEmpty()) {
            Entry entry = new Entry();
            entry.setName(source.name());
            entry.setType(source.qualifiedName());
            context.getDescriptionDocParser().parse(context, source).ifPresent(entry::setDesc);
            entry.setFields(fields);
            entry.setAnnotations(context.getAnnotationDocParser().parse(context, source.annotations()));
            return Optional.of(entry);
        }
        return Optional.empty();
    }

}
