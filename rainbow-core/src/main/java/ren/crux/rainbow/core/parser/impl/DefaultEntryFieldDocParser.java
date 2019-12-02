package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Field;
import ren.crux.rainbow.core.parser.EntryFieldDocParser;
import ren.crux.rainbow.core.reader.parser.Context;

import java.util.Optional;

/**
 * 默认实体属性解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class DefaultEntryFieldDocParser implements EntryFieldDocParser {

    @Override
    public boolean support(@NonNull Context context, @NonNull FieldDoc source) {
        return true;
    }

    @Override
    public Optional<Field> parse0(@NonNull Context context, @NonNull FieldDoc source) {
        Field field = new Field();
        field.setName(source.name());
        field.setType(source.type().qualifiedTypeName());
        context.getDescriptionDocParser().parse(context, source).ifPresent(field::setDesc);
        field.setAnnotations(context.getAnnotationDocParser().parse(context, source.annotations()));
        context.logType(source.type().qualifiedTypeName());
        return Optional.of(field);
    }
}
