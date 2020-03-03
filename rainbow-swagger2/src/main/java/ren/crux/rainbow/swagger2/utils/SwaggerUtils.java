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

package ren.crux.rainbow.swagger2.utils;

import org.springframework.util.ReflectionUtils;
import springfox.documentation.RequestHandler;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.RequestMappingContext;

import java.lang.reflect.Field;

/**
 * SwaggerUtils
 *
 * @author wangzhihui
 **/
public class SwaggerUtils {

    public static RequestMappingContext getRequestMappingContext(OperationContext context) {
        try {
            Field field = OperationContext.class.getDeclaredField("requestContext");
            ReflectionUtils.makeAccessible(field);
            return (RequestMappingContext) ReflectionUtils.getField(field, context);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static RequestHandler getRequestHandler(OperationContext context) {
        return getRequestHandler(getRequestMappingContext(context));
    }

    public static RequestHandler getRequestHandler(RequestMappingContext context) {
        try {
            Field field = RequestMappingContext.class.getDeclaredField("handler");
            ReflectionUtils.makeAccessible(field);
            return (RequestHandler) ReflectionUtils.getField(field, context);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}
