package ren.crux.rainbow.javadoc.utils;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhihui
 */
public class JavaDocHelper {

    public static final String OBJECT_TYPE_NAME = Object.class.getTypeName();

    public static Map<String, FieldDoc> getAllFieldDoc(ClassDoc classDoc) {
        if (!classDoc.isClass()) {
            return Collections.emptyMap();
        }
        Map<String, FieldDoc> fieldDocs = new HashMap<>(16);
        ClassDoc cd = classDoc;
        while (cd != null && !StringUtils.equals(OBJECT_TYPE_NAME, cd.typeName())) {
            FieldDoc[] fields = cd.fields();
            for (FieldDoc fieldDoc : fields) {
                fieldDocs.put(fieldDoc.name(), fieldDoc);
            }
            cd = cd.superclass();
        }
        return fieldDocs;
    }

}
