package ren.crux.rainbow.core.parser;

import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public class EntryParser extends AbstractEnhanceParser<Class<?>, Entry> {

    private final EntryFieldParser entryFieldParser;
    private final EntryMethodParser entryMethodParser;
    private final AnnotationParser annotationParser;
    private final CommentTextParser commentTextParser;

    public EntryParser(EntryFieldParser entryFieldParser,
                       EntryMethodParser entryMethodParser,
                       AnnotationParser annotationParser,
                       CommentTextParser commentTextParser) {
        this.entryFieldParser = entryFieldParser;
        this.entryMethodParser = entryMethodParser;
        this.annotationParser = annotationParser;
        this.commentTextParser = commentTextParser;
    }

    public EntryParser(Interceptor<Class<?>, Entry> interceptor, EntryFieldParser entryFieldParser, EntryMethodParser entryMethodParser, AnnotationParser annotationParser, CommentTextParser commentTextParser) {
        super(interceptor);
        this.entryFieldParser = entryFieldParser;
        this.entryMethodParser = entryMethodParser;
        this.annotationParser = annotationParser;
        this.commentTextParser = commentTextParser;
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<Entry> parse0(Context context, Class<?> source) {
        context.getClassDoc(source.getTypeName());
        Entry entry = new Entry();
        entry.setEnumType(source.isEnum());
        entry.setType(source.getTypeName());
        entry.setSimpleName(source.getSimpleName());
        entry.setName(source.getCanonicalName());
        entry.setAnnotations(annotationParser.parse(context, source.getAnnotations()));
        context.getClassDoc(source).flatMap(classDoc -> commentTextParser.parse(context, classDoc)).ifPresent(entry::setCommentText);
        Field[] fields = EntryUtils.getAllFields(source).toArray(new Field[0]);
        if (fields.length > 0) {
            entry.getFields().addAll(entryFieldParser.parse(context, fields));
        }
        Method[] methods = source.getMethods();
        if (methods.length > 0) {
            entry.getMethods().addAll(entryMethodParser.parse(context, methods));
        }
        if (source.isInterface()) {
            context.getImplClass(entry.getType()).ifPresent(impl -> entry.setImpl(parse(context, impl).orElse(null)));
        }
        return Optional.of(entry);
    }
}
