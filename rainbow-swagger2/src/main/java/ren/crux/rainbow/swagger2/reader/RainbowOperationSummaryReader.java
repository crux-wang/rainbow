/*
 *  Copyright 2020. The Crux Authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
