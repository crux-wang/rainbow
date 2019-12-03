package ren.crux.rainbow.core.desc.reader.parser.filter;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class ClassDocFilter implements DocFilter<ClassDoc> {

    public static final String LOMBOK_DATA_TYPE = "lombok.Data";

    @Override
    public boolean doFilter(ClassDoc doc) {
        if (ArrayUtils.isNotEmpty(doc.annotations())) {
            for (AnnotationDesc annotation : doc.annotations()) {
                if (StringUtils.equals(LOMBOK_DATA_TYPE, annotation.annotationType().typeName())) {
                    return true;
                }
            }
        }
        String name = doc.name();
        if (StringUtils.endsWithAny(name, "Util", "Utils", "Helper", "Controller", "Service", "Interceptor")) {
            return false;
        }
        return true;
    }
}
