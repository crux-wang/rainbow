package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class EntryParser extends AbstractEnhanceParser<Pair<Class<?>, ClassDoc>, Entry> {

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

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Optional<Entry> parse0(Context context, Pair<Class<?>, ClassDoc> source) {
        if (source == null || source.getLeft() == null) {
            return Optional.empty();
        }
        Class<?> cls = source.getLeft();
        ClassDoc classDoc = source.getRight();
        Entry entry = new Entry();
        entry.setInterfaceType(cls.isInterface());
        entry.setEnumType(cls.isEnum());
        entry.setArrayType(cls.isArray());
        entry.setType(cls.getTypeName());
        entry.setSimpleName(cls.getSimpleName());
        entry.setName(cls.getCanonicalName());
        entry.setAnnotations(annotationParser.parse(context, cls.getAnnotations()));
        commentTextParser.parse(context, classDoc).ifPresent(entry::setCommentText);
        Map<String, FieldDoc> fieldDocMap = classDoc == null ? Collections.emptyMap() : context.getClassFieldDocs(classDoc);
        entry.getFields().addAll(entryFieldParser.parse(context, EntryUtils.getAllFields(cls).stream().map(fd -> Pair.of(fd, fieldDocMap.get(fd.getName()))).toArray(Pair[]::new)));
        Map<String, MethodDoc> methodDocMap = classDoc == null ? Collections.emptyMap() : Arrays.stream(classDoc.methods(true)).filter(m -> m.parameters().length == 0).collect(Collectors.toMap(MethodDoc::name, f -> f));
        entry.getMethods().addAll(entryMethodParser.parse(context, Arrays.stream(cls.getMethods()).map(fd -> Pair.of(fd, methodDocMap.get(fd.getName()))).toArray(Pair[]::new)));
        if (entry.isInterfaceType()) {
            context.impl(entry.getType()).ifPresent(impl -> entry.setImpl(parse(context, Pair.of(impl, context.getClassDoc(impl.getTypeName()).orElse(null))).orElse(null)));
        }
        return Optional.of(entry);
    }
}
