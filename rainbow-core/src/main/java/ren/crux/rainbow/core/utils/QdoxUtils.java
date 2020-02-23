package ren.crux.rainbow.core.utils;


import com.thoughtworks.qdox.model.JavaClass;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * QdoxUtils
 *
 * @author wangzhihui
 **/
public class QdoxUtils {

    public static Map<String, JavaClass> toMap(Collection<JavaClass> javaClasses) {
        if (CollectionUtils.isEmpty(javaClasses)) {
            return Collections.emptyMap();
        }
        return javaClasses.stream().collect(Collectors.toMap(JavaClass::getFullyQualifiedName, jc -> jc));
    }
}
