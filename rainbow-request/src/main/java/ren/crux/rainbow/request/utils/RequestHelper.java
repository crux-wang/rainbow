package ren.crux.rainbow.request.utils;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Optional;

public class RequestHelper {

    public static final String REQUEST_MAPPING_TYPE = RequestMapping.class.getTypeName();

    public static Optional<AnnotationDesc> getRequestMappingDesc(ClassDoc classDoc) {
        return Arrays.stream(classDoc.annotations()).filter(a -> REQUEST_MAPPING_TYPE.equals(a.annotationType().typeName())).findFirst();
    }

}
