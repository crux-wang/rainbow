package ren.crux.rainbow.core.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringWebUtils
 *
 * @author wangzhihui
 **/
public class SpringWebUtils {

    public static boolean isController(Class<?> cls) {
        return cls.isAnnotationPresent(RestController.class) || cls.isAnnotationPresent(Controller.class);
    }

    public static String[] getRequestMappingPath(Class<?> cls) {
        RequestMapping requestMapping = cls.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            if (ArrayUtils.isNotEmpty(requestMapping.path())) {
                return requestMapping.path();
            }
            if (ArrayUtils.isNotEmpty(requestMapping.value())) {
                return requestMapping.value();
            }
        }
        return new String[0];
    }
}
