package ren.crux.rainbow.core.utils;

import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.parser.Context;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DocHelper {

    public static List<FieldDoc> getAllFieldDoc(ClassDoc classDoc) {
        if (!classDoc.isClass()) {
            return Collections.emptyList();
        }
        List<FieldDoc> fieldDocs = null;
        ClassDoc cd = classDoc;
        while (cd != null && !StringUtils.equals(Context.OBJECT_TYPE_NAME, cd.typeName())) {
            FieldDoc[] fields = cd.fields();
            if (fields.length > 0) {
                if (fieldDocs == null) {
                    fieldDocs = new LinkedList<>();
                }
                fieldDocs.addAll(Arrays.asList(fields));
            }
            cd = cd.superclass();
        }
        return fieldDocs == null ? Collections.emptyList() : fieldDocs;
    }

    public static boolean hasAnyFieldDoc(ClassDoc classDoc) {
        if (!classDoc.isClass()) {
            return false;
        }
        ClassDoc cd = classDoc;
        while (cd != null && !StringUtils.equals(Context.OBJECT_TYPE_NAME, cd.typeName())) {
            FieldDoc[] fields = cd.fields();
            if (fields.length > 0) {
                return true;
            }
            cd = cd.superclass();
        }
        return false;
    }

    public static Object getValue(AnnotationValue value) {
        if (value.value() instanceof AnnotationValue[]) {
            return Arrays.stream(((AnnotationValue[]) value.value())).map(AnnotationValue::value).toArray();
        } else if (value.value() instanceof AnnotationValue) {
            return ((AnnotationValue) value.value()).value();
        } else {
            return value.value();
        }
    }

}
