package ren.crux.rainbow.runtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ren.crux.rainbow.core.desc.model.ClassDesc;
import ren.crux.rainbow.core.desc.model.FieldDesc;
import ren.crux.rainbow.core.desc.model.MethodDesc;
import ren.crux.rainbow.core.desc.model.ParameterDesc;
import ren.crux.rainbow.core.desc.reader.JavaDocReader;
import ren.crux.rainbow.core.desc.reader.impl.DefaultJavaDocReader;
import ren.crux.rainbow.core.desc.reader.impl.DefaultRootDocParser;
import ren.crux.rainbow.core.desc.reader.parser.RootDocParser;
import ren.crux.rainbow.core.docs.model.RequestMethod;
import ren.crux.rainbow.core.docs.model.RequestParam;
import ren.crux.rainbow.core.docs.model.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ScanHelper {

    private final WebApplicationContext applicationContext;

    public ScanHelper(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Document getRequestGroups(String... entryPackages) {
        Set<String> entryPackageSet;
        if (entryPackages == null) {
            entryPackageSet = Collections.emptySet();
        } else {
            entryPackageSet = Arrays.stream(entryPackages).collect(Collectors.toSet());
        }
        Document document = new Document();
        Set<String> entryClassNames = new HashSet<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        Map<String, RequestGroup> groupMap = new HashMap<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            Type returnType = method.getGenericReturnType();
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            RequestMethod[] requestMethods = methodsCondition.getMethods().stream().map(m -> RequestMethod.valueOf(m.toString())).toArray(RequestMethod[]::new);
            Request request = new Request();
            request.setName(method.getName());
            request.setType(className + "." + method.getName());
            request.setReturnType(returnType.getTypeName());
            entryClassNames.add(returnType.getTypeName());
            request.setActualParamTypes(getActualTypeArguments(returnType));
            request.setMethod(requestMethods);
            request.setPath(p.getPatterns().toArray(new String[0]));
            Parameter[] parameters = method.getParameters();
            List<RequestParam> params = Arrays.stream(parameters).map(parameter -> {
                entryClassNames.add(parameter.getParameterizedType().getTypeName());
                return process(parameter);
            }).collect(Collectors.toList());
            request.setParams(params);
            RequestGroup requestGroup = groupMap.get(className);
            if (requestGroup == null) {
                requestGroup = new RequestGroup();
                requestGroup.setName(method.getDeclaringClass().getSimpleName());
                requestGroup.setType(className);
                groupMap.put(className, requestGroup);
            }
            requestGroup.addRequest(request);
        }
        Map<String, Entry> entryMap = entryClassNames
                .stream()
                .filter(type -> {
                    for (String pkg : entryPackageSet) {
                        if (StringUtils.startsWith(type, pkg)) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(this::process)
                .collect(Collectors.toMap(Entry::getType, e -> e));
        document.setRequestGroups(new LinkedList<>(groupMap.values()));
        document.setEntryMap(entryMap);
        return document;
    }

    public Entry process(String entryClassName) {
        System.out.println("entryClassName = " + entryClassName);
        Entry entry = new Entry();
        entry.setType(entryClassName);
        List<FieldDetail> fields = new LinkedList<>();
        Class<?> cls;
        try {
            cls = Class.forName(entryClassName);
        } catch (ClassNotFoundException e) {
            return null;
        }
        do {
            if (entry.getName() == null) {
                entry.setName(cls.getSimpleName());
            }
            Field[] declaredFields = cls.getDeclaredFields();
            List<FieldDetail> tmp = Arrays.stream(declaredFields).map(this::process).collect(Collectors.toList());
            fields.addAll(tmp);
            cls = cls.getSuperclass();
        } while (!(cls.equals(Object.class)));
        entry.setFields(fields);
        return entry;
    }

    public FieldDetail process(Field field) {
        FieldDetail fieldDetail = new FieldDetail();
        fieldDetail.setName(field.getName());
        fieldDetail.setType(field.getGenericType().getTypeName());
        fieldDetail.setActualParamTypes(getActualTypeArguments(field.getGenericType()));
//        fieldDetail.setAnnotations();
        return fieldDetail;
    }

    public RequestParam process(Parameter parameter) {
        RequestParam requestParam = new RequestParam();
        requestParam.setName(parameter.getName());
        Type parameterType = parameter.getParameterizedType();
        requestParam.setType(parameterType.getTypeName());
        requestParam.setActualParamTypes(getActualTypeArguments(parameterType));
        org.springframework.web.bind.annotation.RequestParam param = parameter.getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        if (param != null) {
            setName(requestParam, param.name(), param.value());
            requestParam.setRequired(param.required());
            requestParam.setDefaultValue(defaultValue(param.defaultValue()));
            requestParam.setParamType(RequestParamType.request_param);
        }
        RequestHeader header = parameter.getAnnotation(RequestHeader.class);
        if (header != null) {
            setName(requestParam, header.name(), header.value());
            requestParam.setRequired(header.required());
            requestParam.setDefaultValue(defaultValue(header.defaultValue()));
            requestParam.setParamType(RequestParamType.request_header);
        }
        RequestAttribute attribute = parameter.getAnnotation(RequestAttribute.class);
        if (attribute != null) {
            setName(requestParam, attribute.name(), attribute.value());
            requestParam.setRequired(attribute.required());
            requestParam.setParamType(RequestParamType.request_attribute);
        }
        RequestBody body = parameter.getAnnotation(RequestBody.class);
        if (body != null) {
            requestParam.setRequired(body.required());
            requestParam.setParamType(RequestParamType.request_body);
        }
        PathVariable pathVariable = parameter.getAnnotation(PathVariable.class);
        if (pathVariable != null) {
            requestParam.setRequired(pathVariable.required());
            setName(requestParam, pathVariable.name(), pathVariable.value());
            requestParam.setParamType(RequestParamType.path_variable);
        }
        SessionAttribute sessionAttribute = parameter.getAnnotation(SessionAttribute.class);
        if (sessionAttribute != null) {
            requestParam.setRequired(sessionAttribute.required());
            setName(requestParam, sessionAttribute.name(), sessionAttribute.value());
            requestParam.setParamType(RequestParamType.session_attribute);
        }
        CookieValue cookieValue = parameter.getAnnotation(CookieValue.class);
        if (cookieValue != null) {
            requestParam.setRequired(cookieValue.required());
            setName(requestParam, cookieValue.name(), cookieValue.value());
            requestParam.setParamType(RequestParamType.cookie_value);
        }
        if (requestParam.getParamType() == null) {
            requestParam.setParamType(RequestParamType.request_param);
            requestParam.setRequired(false);
        }
        return requestParam;

    }

    private String defaultValue(String value) {
        return StringUtils.equals(value, ValueConstants.DEFAULT_NONE) ? null : value;
    }

    private void setName(RequestParam requestParam, String name, String value) {
        String tmp = requestParam.getName();
        if (StringUtils.isNotBlank(name)) {
            tmp = name;
        } else if (StringUtils.isNotBlank(value)) {
            tmp = value;
        }
        requestParam.setName(tmp);
    }

    private String[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) (type)).getActualTypeArguments();
            if (actualTypeArguments != null) {
                return Arrays.stream(actualTypeArguments).map(Type::getTypeName).toArray(String[]::new);
            }
        }
        return null;
    }

    public Document read() throws Exception {
        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-spring-boot-starter/src/test/java/";
        final String[] packageNames = new String[]{"ren.crux.rainbow.runtime.demo"};
        RootDocParser<List<ClassDesc>> rootParser = new DefaultRootDocParser();
        JavaDocReader<List<ClassDesc>> javaDocReader = new DefaultJavaDocReader(rootParser);
        List<ClassDesc> classDescList = javaDocReader.read(path, packageNames).orElseThrow(Exception::new);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        System.out.println(objectMapper.writeValueAsString(classDescList));
//        System.out.println("classDescList.size() = " + classDescList.size());
        Document document = getRequestGroups(packageNames);
//        System.out.println(objectMapper.writeValueAsString(document));
//        System.out.println("~~~~~~");
        return merge(document, classDescList);
    }

    public Document merge(@NonNull Document document, @NonNull List<ClassDesc> descs) {
        Map<String, ClassDesc> dict = descs.stream().collect(Collectors.toMap(ClassDesc::getType, e -> e));
        Map<String, Entry> entryMap = document.getEntryMap();
        List<RequestGroup> requestGroups = document.getRequestGroups();
        entryMap.forEach((type, entry) -> {
            ClassDesc classDesc = dict.get(type);
            if (classDesc != null) {
                entry.setCommentText(classDesc.getCommentText());
                List<FieldDesc> fieldDescs = classDesc.getFields();
                List<FieldDetail> fieldDetails = entry.getFields();
                if (fieldDetails != null && fieldDescs != null) {
                    for (int i = 0; i < fieldDetails.size(); i++) {
                        FieldDetail fieldDetail = fieldDetails.get(i);
                        FieldDesc fieldDesc = fieldDescs.get(i);
                        String typeName = StringUtils.substringBefore(fieldDetail.getType(), "<");
                        if (StringUtils.equals(fieldDesc.getType(), typeName)) {
                            fieldDetail.setCommentText(fieldDesc.getCommentText());
                        } else {
                            log.warn("no matching type name : {}", typeName);
                        }
                    }
                }
            }
        });
        if (requestGroups != null) {
            for (RequestGroup requestGroup : requestGroups) {
                ClassDesc classDesc = dict.get(requestGroup.getType());
                if (classDesc != null) {
                    requestGroup.setCommentText(classDesc.getCommentText());
                    List<MethodDesc> methodDescs = classDesc.getMethods();
                    List<Request> requests = requestGroup.getRequests();
                    if (methodDescs != null && requests != null) {
                        Map<String, MethodDesc> methodDict = methodDescs.stream().collect(Collectors.toMap(MethodDesc::getType, d -> d));
                        for (int i = 0; i < requests.size(); i++) {
                            Request request = requests.get(i);
                            MethodDesc methodDesc = methodDict.get(request.getType());
                            if (methodDesc != null) {
                                request.setCommentText(methodDesc.getCommentText());
                                List<RequestParam> requestParams = request.getParams();
                                List<ParameterDesc> parameterDescs = methodDesc.getParameters();
                                if (requestParams != null && parameterDescs != null) {
                                    for (int j = 0; j < requestParams.size(); j++) {
                                        RequestParam requestParam = requestParams.get(j);
                                        String typeName = StringUtils.substringBefore(requestParam.getType(), "<");
                                        ParameterDesc parameterDesc = parameterDescs.get(j);
                                        if (StringUtils.equals(typeName, parameterDesc.getType())) {
                                            if (StringUtils.startsWith(requestParam.getName(), "arg")) {
                                                requestParam.setName(parameterDesc.getName());
                                            }
                                            requestParam.setCommentText(parameterDesc.getCommentText());
                                        } else {
                                            log.warn("no matching param type name : {}", request.getType());
                                        }
                                    }
                                }
                            } else {
                                log.warn("no matching method type name : {}", request.getType());
                            }
                        }
                    }
                }
            }
        }
        return document;
    }

}
