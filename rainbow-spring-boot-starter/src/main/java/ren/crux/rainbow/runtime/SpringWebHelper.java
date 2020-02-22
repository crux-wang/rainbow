package ren.crux.rainbow.runtime;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.core.utils.EntryUtils;
import ren.crux.raonbow.common.model.RequestParam;
import ren.crux.raonbow.common.model.RequestParamType;

import java.lang.reflect.Parameter;

/**
 * @author wangzhihui
 */
@Slf4j
public class SpringWebHelper {

    public static void setName(RequestParam requestParam, String name, String value) {
        String tmp = requestParam.getName();
        if (StringUtils.isNotBlank(name)) {
            tmp = name;
        } else if (StringUtils.isNotBlank(value)) {
            tmp = value;
        }
        requestParam.setName(tmp);
    }

    public static String[] getRequestPath(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
                if (ArrayUtils.isNotEmpty(requestMapping.path())) {
                    return requestMapping.path();
                }
                if (ArrayUtils.isNotEmpty(requestMapping.value())) {
                    return requestMapping.value();
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("class not found : {}", className, e);
        }
        return new String[0];
    }

    public static RequestParam process(Parameter parameter) {
        RequestParam requestParam = new RequestParam();
        requestParam.setName(parameter.getName());
        requestParam.setType(EntryUtils.build(parameter));
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
