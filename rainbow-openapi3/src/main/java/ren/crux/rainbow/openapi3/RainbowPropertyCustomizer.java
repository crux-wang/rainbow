package ren.crux.rainbow.openapi3;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;


@Component
public class RainbowPropertyCustomizer implements PropertyCustomizer {
    /**
     * @param property to be customized
     * @param type     form the model class
     * @return customized property
     */
    @Override
    public Schema customize(Schema property, AnnotatedType type) {

        return property;
    }
}
