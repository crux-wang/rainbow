package ren.crux.rainbow.runtime;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ren.crux.rainbow.core.desc.model.ClassDesc;
import ren.crux.rainbow.core.desc.utils.DescHelper;
import ren.crux.rainbow.core.docs.model.RequestMethod;
import ren.crux.rainbow.core.docs.model.RequestParam;
import ren.crux.rainbow.core.docs.model.*;
import ren.crux.rainbow.core.docs.utils.MergeHelper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
@Slf4j
public class DocHelper {

    public static Document read(RequestMappingHandlerMapping mapping, String path, String[] packageNames) {
        List<ClassDesc> classDescs = DescHelper.read(path, packageNames);
        Document document = read(mapping, packageNames);
        MergeHelper.merge(document, classDescs);
        return document;
    }

    public static Document read(RequestMappingHandlerMapping mapping, String... entryPackages) {
        Set<String> entryPackageSet;
        if (entryPackages == null) {
            entryPackageSet = Collections.emptySet();
        } else {
            entryPackageSet = Arrays.stream(entryPackages).collect(Collectors.toSet());
        }
        Document document = new Document();
        Set<String> entryClassNames = new HashSet<>();
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        Map<String, RequestGroup> groupMap = new HashMap<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            Request request = read(info, handlerMethod, entryClassNames);
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            RequestGroup requestGroup = groupMap.get(className);
            if (requestGroup == null) {
                requestGroup = new RequestGroup();
                requestGroup.setName(method.getDeclaringClass().getSimpleName());
                requestGroup.setType(className);
                groupMap.put(className, requestGroup);
            }
            requestGroup.addRequest(request);
        }
        document.setRequestGroups(new LinkedList<>(groupMap.values()));
        document.setEntryMap(MergeHelper.build(entryClassNames, entryPackageSet));
        return document;
    }

    public static Request read(RequestMappingInfo info, HandlerMethod handlerMethod, Set<String> entryClassNames) {
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
        request.setActualParamTypes(MergeHelper.getActualTypeArguments(returnType));
        request.setMethod(requestMethods);
        request.setPath(p.getPatterns().toArray(new String[0]));
        Parameter[] parameters = method.getParameters();
        List<RequestParam> params = Arrays.stream(parameters).map(parameter -> {
            entryClassNames.add(parameter.getParameterizedType().getTypeName());
            return process(parameter);
        }).collect(Collectors.toList());
        request.setParams(params);
        return request;
    }

    public static void setName(RequestParam requestParam, String name, String value) {
        String tmp = requestParam.getName();
        if (StringUtils.isNotBlank(name)) {
            tmp = name;
        } else if (StringUtils.isNotBlank(value)) {
            tmp = value;
        }
        requestParam.setName(tmp);
    }

    public static RequestParam process(Parameter parameter) {
        RequestParam requestParam = new RequestParam();
        requestParam.setName(parameter.getName());
        Type parameterType = parameter.getParameterizedType();
        requestParam.setType(parameterType.getTypeName());
        requestParam.setActualParamTypes(MergeHelper.getActualTypeArguments(parameterType));
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

    private static String defaultValue(String value) {
        return StringUtils.equals(value, ValueConstants.DEFAULT_NONE) ? null : value;
    }

}
