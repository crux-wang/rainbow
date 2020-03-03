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

package ren.crux.rainbow.openapi3;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import java.lang.annotation.Annotation;

/**
 * RainbowPropertyCustomizer
 *
 * @author wangzhihui
 */
@Component
public class RainbowPropertyCustomizer implements PropertyCustomizer {
    /**
     * @param property to be customized
     * @param type     form the model class
     * @return customized property
     */
    @Override
    public Schema customize(Schema property, AnnotatedType type) {
        Annotation[] ctxAnnotations = type.getCtxAnnotations();
        if (ctxAnnotations != null) {
            for (Annotation annotation : ctxAnnotations) {
                switch (annotation.annotationType().getName()) {
                    case "org.hibernate.validator.constraints.URL":
                        property.format("url");
                        break;
                    case "javax.validation.constraints.Email":
                        property.format("mail");
                        if (property instanceof StringSchema) {
                            property.setPattern(((Email) annotation).regexp());
                        }
                        break;
                }
            }
        }
        return property;
    }
}
