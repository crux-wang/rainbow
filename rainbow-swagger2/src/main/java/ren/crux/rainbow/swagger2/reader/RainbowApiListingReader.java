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
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;
import ren.crux.rainbow.swagger2.RainbowSwaggerPluginSupport;
import springfox.documentation.service.ResourceGroup;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingBuilderPlugin;
import springfox.documentation.spi.service.contexts.ApiListingContext;

@Component
@Order(RainbowSwaggerPluginSupport.RAINBOW_SWAGGER_PLUGIN_ORDER)
public class RainbowApiListingReader implements ApiListingBuilderPlugin {
    @Override
    public void apply(ApiListingContext apiListingContext) {
        ResourceGroup group = apiListingContext.getResourceGroup();
        String description = group.getControllerClass()
                .transform(RuntimeJavadocUtils::getComment)
                .or(group.getGroupName());

        apiListingContext.apiListingBuilder()
                .description(description);
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }
}
