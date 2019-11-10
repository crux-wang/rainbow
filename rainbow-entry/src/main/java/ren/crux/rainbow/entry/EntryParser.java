package ren.crux.rainbow.entry;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.EntryDocParser;

import java.util.Optional;

import static ren.crux.rainbow.core.parser.Context.OBJECT_TYPE_NAME;

/**
 * @author wangzhihui
 */
public class EntryParser implements EntryDocParser {
    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
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
        context.getTagDocParser().ifPresent(p -> {
            for (Tag tag : source.tags()) {
                entry.addLink(p.parse(context, tag));
            }
            for (Tag tag : source.inlineTags()) {
                entry.addInlineLink(p.parse(context, tag));
            }
        });
        context.getFieldDocParser().ifPresent(p -> {
            FieldDoc[] fields = source.fields();
            for (FieldDoc field : fields) {
                entry.addField(p.parse(context, field));
            }
        });
        context.logEntry(entry);
        if (!StringUtils.equals(source.superclassType().qualifiedTypeName(), OBJECT_TYPE_NAME)) {
            Entry superEntry = parse(context, source.superclass());
            entry.addSupperEntryLink(context.getLink(superEntry));
        }
        return entry;
    }
}
