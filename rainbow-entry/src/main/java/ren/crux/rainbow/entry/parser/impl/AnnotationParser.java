package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeElementDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.model.Annotation;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnnotationParser implements AnnotationDocParser {

    @Override
    public boolean support(@NonNull Context context, @NonNull AnnotationDesc source) {
        return true;
    }

    @Override
    public Optional<Annotation> parse(@NonNull Context context, @NonNull AnnotationDesc source) {
        Annotation annotation = new Annotation();
        annotation.setType(source.annotationType().qualifiedName());
        annotation.setName(source.annotationType().name());
        Map<String, Object> attribute = null;
        for (AnnotationDesc.ElementValuePair elementValuePair : source.elementValues()) {
            AnnotationTypeElementDoc element = elementValuePair.element();
            Object value = elementValuePair.value().value();
            if (attribute == null) {
                attribute = new HashMap<>();
            }
            attribute.put(element.name(), value == null ? element.defaultValue() : value);
        }
        if (attribute != null) {
            annotation.setAttribute(attribute);
        }
        return Optional.of(annotation);
    }
}
