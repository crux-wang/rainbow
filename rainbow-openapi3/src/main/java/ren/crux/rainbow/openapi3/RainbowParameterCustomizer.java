package ren.crux.rainbow.openapi3;

import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.ParameterCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.openapi3.utils.ReflectUtils;

import java.lang.reflect.Method;

/**
 * RainbowParameterCustomizer
 *
 * @author wangzhihui
 */
@Slf4j
@Component
@Order
public class RainbowParameterCustomizer implements ParameterCustomizer {
    /**
     * @param parameterModel to be customized
     * @param parameter      original parameter from handler method
     * @param handlerMethod  handler method
     * @return customized parameter
     */
    @Override
    public Parameter customize(Parameter parameterModel, java.lang.reflect.Parameter parameter, HandlerMethod handlerMethod) {
        // request body 类型 parameterModel = null
        if (parameterModel != null) {
            Method method = handlerMethod.getMethod();
            int parameterIndex = ReflectUtils.getParameterIndex(parameter);
            RuntimeJavadocUtils.getParamComment(method, parameterIndex)
                    .ifPresent(parameterModel::description);
            if (StringUtils.equals("org.springframework.data.domain.Pageable", parameter.getType().getName())) {
                parameterModel.deprecated(true);
            }
        }
        return parameterModel;
    }
}
