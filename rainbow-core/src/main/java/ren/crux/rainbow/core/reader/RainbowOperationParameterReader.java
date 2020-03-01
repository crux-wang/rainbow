package ren.crux.rainbow.core.reader;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ren.crux.rainbow.core.RainbowSwaggerPluginSupport;
import ren.crux.rainbow.core.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.core.utils.SwaggerUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

/**
 * RainbowOperationImplicitParameterReader
 *
 * @author wangzhihui
 * @see springfox.documentation.spring.web.readers.operation.OperationParameterReader
 * @see springfox.documentation.swagger.readers.parameter.ApiParamParameterBuilder
 **/
@Component
@Order(RainbowSwaggerPluginSupport.RAINBOW_SWAGGER_PLUGIN_ORDER)
public class RainbowOperationParameterReader implements ParameterBuilderPlugin {

    @Override
    public void apply(ParameterContext parameterContext) {
        RequestHandler requestHandler = SwaggerUtils.getRequestHandler(parameterContext.getOperationContext());
        HandlerMethod handlerMethod = requestHandler.getHandlerMethod();
        ResolvedMethodParameter resolvedMethodParameter = parameterContext.resolvedMethodParameter();
        String paramName = resolvedMethodParameter.defaultName().or("");
        RuntimeJavadocUtils.getParamComment(handlerMethod.getMethod(), paramName)
                .ifPresent(cmt -> parameterContext.parameterBuilder().description(cmt));
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}