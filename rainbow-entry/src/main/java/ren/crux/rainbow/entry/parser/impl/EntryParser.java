package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.ClassDoc;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.EntryDocParser;
import ren.crux.rainbow.entry.parser.TagDocParser;

import java.util.Optional;

import static ren.crux.rainbow.core.parser.Context.OBJECT_TYPE_NAME;

/**
 * @author wangzhihui
 */
public class EntryParser implements EntryDocParser {

    private TagDocParser tagParser = new TagParser();

    @Override
    public boolean support(Context context, ClassDoc source) {
        return source.isClass();
    }

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<Entry> parse(Context context, ClassDoc source) {
        Optional<Entry> optional = context.getEntry(source.qualifiedName());
        if (optional.isPresent()) {
            return optional;
        } else {
            return parse0(context, source);
        }
    }

    private Optional<Entry> parse0(Context context, ClassDoc source) {
        Entry entry = new Entry();
        entry.setName(source.name());
        entry.setQualifiedName(source.qualifiedName());
        entry.setDescription(source.getRawCommentText());
//        context.getTagDocParser().ifPresent(p -> {
//            for (Tag tag : source.tags()) {
//                p.parse(context, tag).ifPresent(entry::addLink);
//            }
//            for (Tag tag : source.inlineTags()) {
//                p.parse(context, tag).ifPresent(entry::addInlineLink);
//            }
//        });
//        context.getFieldDocParser().ifPresent(p -> {
//            FieldDoc[] fields = source.fields();
//            for (FieldDoc field : fields) {
//                p.parse(context, field).ifPresent(entry::addField);
//            }
//        });
        context.logEntry(entry);
        if (!StringUtils.equals(source.superclassType().qualifiedTypeName(), OBJECT_TYPE_NAME)) {
            parse(context, source.superclass()).ifPresent(superEntry -> entry.addSupperEntryLink(context.getLink(superEntry)));
        }
        return Optional.of(entry);
    }
}
