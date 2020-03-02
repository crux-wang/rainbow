package ren.crux.rainbow.openapi3;

import com.fasterxml.jackson.databind.JavaType;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;


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
        JavaType javaType = Json.mapper().constructType(type.getType());
        return property;
    }
}
