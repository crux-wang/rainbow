package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.desc.model.ClassDesc;
import ren.crux.rainbow.core.desc.model.Describable;
import ren.crux.rainbow.core.desc.model.FieldDesc;
import ren.crux.rainbow.core.desc.model.MethodDesc;
import ren.crux.rainbow.core.desc.reader.parser.*;
import ren.crux.rainbow.core.desc.utils.DocHelper;

import java.util.List;
import java.util.Optional;

public class ClassDescParser implements ClassDocParser<ClassDesc> {

    public static final ClassDescParser INSTANCE = new ClassDescParser();

    private final MethodDocParser<MethodDesc> methodDescParser = MethodDescParser.INSTANCE;
    private final FieldDocParser<FieldDesc> fieldDescParser = FieldDescParser.INSTANCE;
    private final CommonDocParser<Describable> descriptionDocParser = DescriptionDocParser.INSTANCE;

    @Override
    public Optional<ClassDesc> parse(@NonNull Context context, @NonNull ClassDoc source) {
        if (context.getClassDocFilter().doFilter(source)) {
            ClassDesc classDesc = new ClassDesc();
            descriptionDocParser.parse(context, source).ifPresent(classDesc::setDesc);
            List<FieldDoc> fields = DocHelper.getAllFieldDoc(source);
            if (CollectionUtils.isNotEmpty(fields)) {
                classDesc.setFields(fieldDescParser.parse(context, fields.toArray(new FieldDoc[0])));
            }
            MethodDoc[] methods = source.methods();
            if (ArrayUtils.isNotEmpty(methods)) {
                classDesc.setMethods(methodDescParser.parse(context, methods));
            }
            if (CollectionUtils.isNotEmpty(classDesc.getFields()) || CollectionUtils.isNotEmpty(classDesc.getMethods())) {
                return Optional.of(classDesc);
            }
        }
        return Optional.empty();
    }
}
