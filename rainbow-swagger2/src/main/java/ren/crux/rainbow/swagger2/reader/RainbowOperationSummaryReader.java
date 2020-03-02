package ren.crux.rainbow.swagger2.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.swagger2.RainbowSwaggerPluginSupport;
import ren.crux.rainbow.swagger2.utils.SwaggerUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * RainbowOperationSummaryReader
 *
 * @author wangzhihui
 * @see OperationBuilderPlugin
 * @see springfox.documentation.swagger.readers.operation.OperationSummaryReader
 **/
@Slf4j
@Component
@Order(RainbowSwaggerPluginSupport.RAINBOW_SWAGGER_PLUGIN_ORDER)
public class RainbowOperationSummaryReader implements OperationBuilderPlugin {

    private final DescriptionResolver descriptions;

    @Autowired
    public RainbowOperationSummaryReader(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(OperationContext context) {
        RequestHandler requestHandler = SwaggerUtils.getRequestHandler(context);
        HandlerMethod handlerMethod = requestHandler.getHandlerMethod();
        String comment = RuntimeJavadocUtils.getComment(handlerMethod.getMethod());
        context.operationBuilder().summary(descriptions.resolve(comment));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}