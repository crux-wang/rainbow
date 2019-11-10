package ren.crux.rainbow.core.old.parser;

import com.sun.javadoc.ClassDoc;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.JavaDocParser;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public class EntryParser implements JavaDocParser<ClassDoc, Entry> {

    private static final String OBJECT_TYPE_NAME = Object.class.getTypeName();

    @Override
    public boolean condition(Context context, ClassDoc source) {
        return source.isEnum() || source.fields().length > 0;
    }

    @Override
    public Entry parse(Context context, ClassDoc source) {
        Optional<Entry> optional = context.getEntry(source.qualifiedName());
        if (optional.isPresent()) {
            return optional.get();
        }
        Entry entry = new Entry();
        entry.setName(source.name());
        entry.setQualifiedName(source.qualifiedName());
        entry.setDescription(source.getRawCommentText());
        entry.setLink(context.parse(source.tags()));
        entry.setInlineLink(context.parse(source.inlineTags()));
        entry.setField(context.parse(source.fields()));
        context.logEntry(entry);
        if (!StringUtils.equals(source.superclassType().qualifiedTypeName(), OBJECT_TYPE_NAME)) {
            Entry superEntry = parse(context, source.superclass());
            entry.addSupperEntryLink(context.getRef(superEntry));
        }
        return entry;
    }
}
