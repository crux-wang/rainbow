package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeElementDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.utils.DocHelper;
import ren.crux.rainbow.entry.model.Annotation;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class AnnotationParser implements AnnotationDocParser {

    @Override
    public boolean support(@NonNull Context context, @NonNull AnnotationDesc source) {
        return true;
    }

    @Override
    public Optional<Annotation> parse(@NonNull Context context, @NonNull AnnotationDesc source) {
        log.debug("parse anno : {}", source.annotationType().name());
        Annotation annotation = new Annotation();
        annotation.setType(source.annotationType().qualifiedName());
        annotation.setName(source.annotationType().name());
        Map<String, Object> attribute = null;
        for (AnnotationDesc.ElementValuePair elementValuePair : source.elementValues()) {
            AnnotationTypeElementDoc element = elementValuePair.element();
            Object value = DocHelper.getValue(elementValuePair.value());
            if (attribute == null) {
                attribute = new HashMap<>();
            }
            attribute.put(element.name(), value == null ? DocHelper.getValue(element.defaultValue()) : value);
        }
        if (attribute != null) {
            annotation.setAttribute(attribute);
        }
        System.out.println(attribute);
        return Optional.of(annotation);
    }
}
