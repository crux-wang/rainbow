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

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spi.service.contexts.OperationContext;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SpringWebUtils
 *
 * @author wangzhihui
 **/
public class SpringWebUtils {

    public static final List<Class<? extends Annotation>> MAPPINGS_ANNOTATION;

    static {
        MAPPINGS_ANNOTATION = Collections.unmodifiableList(Stream.of(
                RequestMapping.class,
                GetMapping.class,
                PostMapping.class,
                PutMapping.class,
                DeleteMapping.class,
                PatchMapping.class
        ).collect(Collectors.toList()));
    }


    public static boolean isController(Class<?> cls) {
        return cls.isAnnotationPresent(RestController.class) || cls.isAnnotationPresent(Controller.class);
    }

    public static String[] getRequestMappingPath(Class<?> cls) {
        RequestMapping requestMapping = cls.getAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            if (ArrayUtils.isNotEmpty(requestMapping.path())) {
                return requestMapping.path();
            }
            if (ArrayUtils.isNotEmpty(requestMapping.value())) {
                return requestMapping.value();
            }
        }
        return new String[0];
    }

    public static Optional<HttpMethod> getRequestMethod(OperationContext context) {
        com.google.common.base.Optional<HttpMethod> optional = context.findAnnotation(GetMapping.class).transform(m -> HttpMethod.GET);
        if (!optional.isPresent()) {
            optional = context.findAnnotation(PostMapping.class).transform(m -> HttpMethod.POST);
        }
        if (!optional.isPresent()) {
            optional = context.findAnnotation(PutMapping.class).transform(m -> HttpMethod.PUT);
        }
        if (!optional.isPresent()) {
            optional = context.findAnnotation(DeleteMapping.class).transform(m -> HttpMethod.DELETE);
        }
        if (!optional.isPresent()) {
            optional = context.findAnnotation(PatchMapping.class).transform(m -> HttpMethod.PATCH);
        }
        return optional.toJavaUtil();
    }
}
