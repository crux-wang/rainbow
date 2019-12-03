package ren.crux.rainbow.runtime;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ren.crux.rainbow.core.docs.model.RequestMethod;
import ren.crux.rainbow.core.docs.model.RequestParam;
import ren.crux.rainbow.core.docs.model.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScanHelper {

    private final WebApplicationContext applicationContext;

    public ScanHelper(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public List<RequestGroup> getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        Map<String, RequestGroup> groupMap = new HashMap<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            PatternsRequestCondition p = info.getPatternsCondition();
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            Class<?> returnType = method.getReturnType();
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            RequestMethod[] requestMethods = methodsCondition.getMethods().stream().map(m -> RequestMethod.valueOf(m.toString())).toArray(RequestMethod[]::new);
            Request request = new Request();
            request.setReturnType(returnType.getCanonicalName());
            request.setActualParamTypes(getActualTypeArguments(returnType));
            request.setMethod(requestMethods);
            request.setPath(p.getPatterns().toArray(new String[0]));
            Parameter[] parameters = method.getParameters();
            List<RequestParam> params = Arrays.stream(parameters).map(this::process).collect(Collectors.toList());
            request.setParams(params);
            RequestGroup requestGroup = groupMap.get(className);
            if (requestGroup == null) {
                requestGroup = new RequestGroup();
                requestGroup.setName(className);
                groupMap.put(className, requestGroup);
            }
            requestGroup.addRequest(request);
        }
        return new LinkedList<>(groupMap.values());
    }

    public RequestParam process(Parameter parameter) {
        RequestParam requestParam = new RequestParam();
        requestParam.setName(parameter.getName());
        Class<?> parameterType = parameter.getType();
        requestParam.setType(parameterType.getCanonicalName());
        requestParam.setActualParamTypes(getActualTypeArguments(parameterType));
        org.springframework.web.bind.annotation.RequestParam param = parameter.getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
        if (param != null) {
            setName(requestParam, param.name(), param.value());
            requestParam.setRequired(param.required());
            requestParam.setDefaultValue(param.defaultValue());
            requestParam.setParamType(RequestParamType.request_param);
        }
        RequestHeader header = parameter.getAnnotation(RequestHeader.class);
        if (header != null) {
            setName(requestParam, header.name(), header.value());
            requestParam.setRequired(header.required());
            requestParam.setDefaultValue(header.defaultValue());
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

    private void setName(RequestParam requestParam, String name, String value) {
        String tmp = requestParam.getName();
        if (StringUtils.isNotBlank(name)) {
            tmp = name;
        } else if (StringUtils.isNotBlank(value)) {
            tmp = value;
        }
        requestParam.setName(tmp);
    }

    private String[] getActualTypeArguments(Class<?> type) {
        Type[] genericInterfaces = type.getGenericInterfaces();
        if (ArrayUtils.isNotEmpty(genericInterfaces)) {
            List<String> types = new LinkedList<>();
            Arrays.stream(genericInterfaces)
                    .filter(is -> is instanceof ParameterizedType)
                    .map(is -> ((ParameterizedType) is).getActualTypeArguments())
                    .forEach(ts -> {
                        for (Type t : ts) {
                            types.add(t.getTypeName());
                        }
                    });
            if (!types.isEmpty()) {
                return types.toArray(new String[0]);
            }
        }
        return null;
    }

}
