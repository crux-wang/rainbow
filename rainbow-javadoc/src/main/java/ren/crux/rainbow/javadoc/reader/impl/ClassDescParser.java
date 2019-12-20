package ren.crux.rainbow.javadoc.reader.impl;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.javadoc.model.ClassDesc;
import ren.crux.rainbow.javadoc.model.CommentText;
import ren.crux.rainbow.javadoc.model.FieldDesc;
import ren.crux.rainbow.javadoc.model.MethodDesc;
import ren.crux.rainbow.javadoc.reader.parser.*;
import ren.crux.rainbow.javadoc.utils.DescHelper;

import java.util.List;
import java.util.Optional;

public class ClassDescParser implements ClassDocParser<ClassDesc> {

    public static final ClassDescParser INSTANCE = new ClassDescParser();

    private final MethodDocParser<MethodDesc> methodDescParser = MethodDescParser.INSTANCE;
    private final FieldDocParser<FieldDesc> fieldDescParser = FieldDescParser.INSTANCE;
    private final CommonDocParser<CommentText> descriptionDocParser = CommentTextParser.INSTANCE;

    @Override
    public Optional<ClassDesc> parse(@NonNull Context context, @NonNull ClassDoc source) {
        if (context.doFilter(source)) {
            ClassDesc classDesc = new ClassDesc();
            classDesc.setName(source.name());
            classDesc.setType(source.qualifiedTypeName());
            descriptionDocParser.parse(context, source).ifPresent(classDesc::setCommentText);
            List<FieldDoc> fields = DescHelper.getAllFieldDoc(source);
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
