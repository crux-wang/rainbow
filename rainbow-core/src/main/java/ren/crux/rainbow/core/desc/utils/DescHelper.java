package ren.crux.rainbow.core.desc.utils;

import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.desc.model.ClassDesc;
import ren.crux.rainbow.core.desc.reader.JavaDocReader;
import ren.crux.rainbow.core.desc.reader.impl.DefaultJavaDocReader;
import ren.crux.rainbow.core.desc.reader.impl.DefaultRootDocParser;
import ren.crux.rainbow.core.desc.reader.parser.Context;
import ren.crux.rainbow.core.desc.reader.parser.RootDocParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
public class DescHelper {

    public static List<ClassDesc> read(String path, String[] packageNames) {
        RootDocParser<List<ClassDesc>> rootParser = new DefaultRootDocParser();
        JavaDocReader<List<ClassDesc>> javaDocReader = new DefaultJavaDocReader(rootParser);
        return javaDocReader.read(path, packageNames).orElse(Collections.emptyList());
    }

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
