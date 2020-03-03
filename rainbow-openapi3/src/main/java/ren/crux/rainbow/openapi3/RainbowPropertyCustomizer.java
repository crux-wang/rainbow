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
