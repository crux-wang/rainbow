package ren.crux.rainbow.request.utils;

import com.sun.javadoc.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.core.utils.DocHelper;
import ren.crux.rainbow.request.parser.RequestDocParser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequestHelper {

    public static final String REQUEST_MAPPING_TYPE = RequestMapping.class.getTypeName();

    public static Optional<RequestMethod> getMethod(String requestMappingType) {
        RequestMethod method = null;
        if (StringUtils.equals(requestMappingType, GetMapping.class.getTypeName())) {
            method = RequestMethod.GET;
        } else if (StringUtils.equals(requestMappingType, PostMapping.class.getTypeName())) {
            method = RequestMethod.POST;
        } else if (StringUtils.equals(requestMappingType, PutMapping.class.getTypeName())) {
            method = RequestMethod.PUT;
        } else if (StringUtils.equals(requestMappingType, DeleteMapping.class.getTypeName())) {
            method = RequestMethod.DELETE;
        } else if (StringUtils.equals(requestMappingType, PutMapping.class.getTypeName())) {
            method = RequestMethod.PUT;
        }
        return Optional.ofNullable(method);
    }

    public static Optional<AnnotationDesc> getRequestMappingDesc(ClassDoc classDoc) {
        return Arrays.stream(classDoc.annotations()).filter(a -> REQUEST_MAPPING_TYPE.equals(a.annotationType().qualifiedName())).findFirst();
    }

    public static List<AnnotationDesc> getAllRequestMappingDesc(MethodDoc methodDoc) {
        return Arrays.stream(methodDoc.annotations()).filter(a -> RequestDocParser.MAPPING_TYPES.contains(a.annotationType().qualifiedName())).collect(Collectors.toList());
    }

    public static Object get(AnnotationDesc annotationDesc, String name, boolean useDefaultValue) {
        Object defaultValue = null;
        for (AnnotationDesc.ElementValuePair elementValuePair : annotationDesc.elementValues()) {
            AnnotationTypeElementDoc element = elementValuePair.element();
            AnnotationValue value = elementValuePair.value();
            if (StringUtils.equals(element.name(), name)) {
                Object val = DocHelper.getValue(value);
                if (val != null) {
                    return val;
                }
            } else if (StringUtils.equals(element.name(), "value")) {
                defaultValue = DocHelper.getValue(value);
            }
        }
        return defaultValue;
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


    public static List<ren.crux.rainbow.request.model.RequestMapping> getAllRequestMapping(MethodDoc methodDoc) {
        List<AnnotationDesc> requestMappingDesc = RequestHelper.getAllRequestMappingDesc(methodDoc);
        return requestMappingDesc.stream().map(desc -> {
            Object pathObj = RequestHelper.get(desc, "path", true);
            String[] path = null;
            if (pathObj != null) {
                if (pathObj instanceof Object[]) {
                    path = Arrays.stream((Object[]) pathObj).filter(Objects::nonNull).map(String::valueOf).toArray(String[]::new);
                }
            }
            String[] method = null;
            if (StringUtils.equals(desc.annotationType().qualifiedName(), REQUEST_MAPPING_TYPE)) {
                Object methodObj = RequestHelper.get(desc, "method", false);
                if (methodObj != null) {
                    if (methodObj instanceof Object[]) {
                        method = Arrays.stream((Object[]) methodObj).filter(Objects::nonNull).map(String::valueOf).toArray(String[]::new);
                    }
                }
            } else {
                Optional<RequestMethod> optional = getMethod(desc.annotationType().qualifiedTypeName());
                if (optional.isPresent()) {
                    method = new String[]{String.valueOf(optional.get())};
                }
            }
            if (path != null && method != null) {
                return new ren.crux.rainbow.request.model.RequestMapping(method, path);
            } else {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());


    }
}
