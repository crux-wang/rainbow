package ren.crux.rainbow.request.utils;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeElementDoc;
import com.sun.javadoc.AnnotationValue;
import com.sun.javadoc.ClassDoc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class RequestHelper {

    public static final String REQUEST_MAPPING_TYPE = RequestMapping.class.getTypeName();

    public static Optional<AnnotationDesc> getRequestMappingDesc(ClassDoc classDoc) {
        return Arrays.stream(classDoc.annotations()).filter(a -> REQUEST_MAPPING_TYPE.equals(a.annotationType().qualifiedName())).findFirst();
    }

    public static Object get(AnnotationDesc annotationDesc, String name, boolean useDefaultValue) {
        Object defaultValue = null;
        for (AnnotationDesc.ElementValuePair elementValuePair : annotationDesc.elementValues()) {
            AnnotationTypeElementDoc element = elementValuePair.element();
            AnnotationValue value = elementValuePair.value();
            if (StringUtils.equals(element.name(), name)) {
                Object val = getValue(value);
                if (val != null) {
                    return val;
                }
            } else if (StringUtils.equals(element.name(), "value")) {
                defaultValue = getValue(value);
            }
        }
        return defaultValue;
    }

    private static Object getValue(AnnotationValue value) {
        if (value.value() instanceof AnnotationValue[]) {
            return Arrays.stream(((AnnotationValue[]) value.value())).map(AnnotationValue::value).toArray();
        } else if (value.value() instanceof AnnotationValue) {
            return ((AnnotationValue) value.value()).value();
        } else {
            return value.value();
        }
    }

    public static Optional<String[]> getRequestMappingPath(ClassDoc classDoc) {
        return RequestHelper.getRequestMappingDesc(classDoc).map(annotationDesc -> {
            Object path = RequestHelper.get(annotationDesc, "path", true);
            if (path != null) {
                if (path instanceof Object[]) {
                    return Arrays.stream((Object[]) path).filter(Objects::nonNull).map(String::valueOf).toArray(String[]::new);
                }
            }
            return null;
        });
    }

}
