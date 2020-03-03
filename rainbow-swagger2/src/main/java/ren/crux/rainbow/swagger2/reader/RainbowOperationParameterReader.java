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

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.swagger2.RainbowSwaggerPluginSupport;
import ren.crux.rainbow.swagger2.utils.SwaggerUtils;
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