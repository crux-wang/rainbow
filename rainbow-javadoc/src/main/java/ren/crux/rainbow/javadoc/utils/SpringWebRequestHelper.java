//package ren.crux.rainbow.javadoc.utils;
//
//import com.sun.javadoc.*;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author wangzhihui
// */
//public class SpringWebRequestHelper {
//
//    public static final String REQUEST_MAPPING_TYPE = "org.springframework.web.bind.annotation.RequestMapping";
//    public static final String GET_MAPPING_TYPE = "org.springframework.web.bind.annotation.GetMapping";
//    public static final String POST_MAPPING_TYPE = "org.springframework.web.bind.annotation.PostMapping";
//    public static final String DELETE_MAPPING_TYPE = "org.springframework.web.bind.annotation.DeleteMapping";
//    public static final String PUT_MAPPING_TYPE = "org.springframework.web.bind.annotation.PutMapping";
//    public static final String PATCH_MAPPING_TYPE = "org.springframework.web.bind.annotation.PatchMapping";
//
//    public static final String REST_CONTROlLER_TYPE = "org.springframework.web.bind.annotation.RestController";
//
//
//    public static final String REQUEST_PARAM_TYPE = "org.springframework.web.bind.annotation.RequestParam";
//    public static final String REQUEST_HEADER_TYPE = "org.springframework.web.bind.annotation.RequestHeader";
//    public static final String REQUEST_ATTRIBUTE_TYPE = "org.springframework.web.bind.annotation.RequestAttribute";
//    public static final String PATH_VARIABLE_TYPE = "org.springframework.web.bind.annotation.PathVariable";
//    public static final String COOKIE_VALUE_TYPE = "org.springframework.web.bind.annotation.CookieValue";
//    public static final String SESSION_ATTRIBUTE_TYPE = "org.springframework.web.bind.annotation.SessionAttribute";
//    public static final String REQUEST_BODY_TYPE = "org.springframework.web.bind.annotation.RequestBody";
//
//
//    public static final Set<String> MAPPING_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
//            REQUEST_MAPPING_TYPE,
//            GET_MAPPING_TYPE,
//            POST_MAPPING_TYPE,
//            DELETE_MAPPING_TYPE,
//            PUT_MAPPING_TYPE,
//            PATCH_MAPPING_TYPE
//    )));
//
//    public static Optional<RequestMethod> getMethod(String requestMappingType) {
//        RequestMethod method = null;
//        if (StringUtils.equals(requestMappingType, GET_MAPPING_TYPE)) {
//            method = RequestMethod.GET;
//        } else if (StringUtils.equals(requestMappingType, POST_MAPPING_TYPE)) {
//            method = RequestMethod.POST;
//        } else if (StringUtils.equals(requestMappingType, DELETE_MAPPING_TYPE)) {
//            method = RequestMethod.DELETE;
//        } else if (StringUtils.equals(requestMappingType, PUT_MAPPING_TYPE)) {
//            method = RequestMethod.PUT;
//        } else if (StringUtils.equals(requestMappingType, PATCH_MAPPING_TYPE)) {
//            method = RequestMethod.PATCH;
//        }
//        return Optional.ofNullable(method);
//    }
//
//    public static Optional<AnnotationDesc> getRequestMappingDesc(ClassDoc classDoc) {
//        return Arrays.stream(classDoc.annotations()).filter(a -> REQUEST_MAPPING_TYPE.equals(a.annotationType().qualifiedName())).findFirst();
//    }
//
//    public static List<AnnotationDesc> getAllRequestMappingDesc(MethodDoc methodDoc) {
//        return Arrays.stream(methodDoc.annotations()).filter(a -> MAPPING_TYPES.contains(a.annotationType().qualifiedName())).collect(Collectors.toList());
//    }
//
//    public static Object get(AnnotationDesc annotationDesc, String name, boolean useDefaultValue) {
//        Object defaultValue = null;
//        for (AnnotationDesc.ElementValuePair elementValuePair : annotationDesc.elementValues()) {
//            AnnotationTypeElementDoc element = elementValuePair.element();
//            AnnotationValue value = elementValuePair.value();
//            if (StringUtils.equals(element.name(), name)) {
//                Object val = DocHelper.getValue(value);
//                if (val != null) {
//                    return val;
//                }
//            } else if (StringUtils.equals(element.name(), "value")) {
//                defaultValue = DocHelper.getValue(value);
//            }
//        }
//        return defaultValue;
//    }
//
//    public static Optional<String[]> getRequestMappingPath(ClassDoc classDoc) {
//        return SpringWebRequestHelper.getRequestMappingDesc(classDoc).map(annotationDesc -> {
//            Object path = SpringWebRequestHelper.get(annotationDesc, "path", true);
//            if (path != null) {
//                if (path instanceof Object[]) {
//                    return Arrays.stream((Object[]) path).filter(Objects::nonNull).map(String::valueOf).toArray(String[]::new);
//                }
//            }
//            return null;
//        });
//    }
//
//
//    public static List<ren.crux.rainbow.javadoc.model.RequestMapping> getAllRequestMapping(MethodDoc methodDoc) {
//        List<AnnotationDesc> requestMappingDesc = SpringWebRequestHelper.getAllRequestMappingDesc(methodDoc);
//        return requestMappingDesc.stream().map(desc -> {
//            Object pathObj = SpringWebRequestHelper.get(desc, "path", true);
//            String[] path = null;
//            if (pathObj != null) {
//                if (pathObj instanceof Object[]) {
//                    path = Arrays.stream((Object[]) pathObj).filter(Objects::nonNull).map(String::valueOf).toArray(String[]::new);
//                }
//            }
//            String[] method = null;
//            if (StringUtils.equals(desc.annotationType().qualifiedName(), REQUEST_MAPPING_TYPE)) {
//                Object methodObj = SpringWebRequestHelper.get(desc, "method", false);
//                if (methodObj != null) {
//                    if (methodObj instanceof Object[]) {
//                        method = Arrays.stream((Object[]) methodObj).filter(Objects::nonNull).map(String::valueOf).toArray(String[]::new);
//                    }
//                }
//            } else {
//                Optional<RequestMethod> optional = getMethod(desc.annotationType().qualifiedTypeName());
//                if (optional.isPresent()) {
//                    method = new String[]{String.valueOf(optional.get())};
//                }
//            }
//            if (path != null && method != null) {
//                return new ren.crux.rainbow.javadoc.model.RequestMapping(method, path);
//            } else {
//                return null;
//            }
//        }).filter(Objects::nonNull).collect(Collectors.toList());
//    }
//}
