package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeElementDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Annotation;
import ren.crux.rainbow.core.parser.AnnotationDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.utils.DocHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 抽象注解文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class DefaultAnnotationDocParser implements AnnotationDocParser {

    @Override
    public Optional<Annotation> parse(@NonNull Context context, @NonNull AnnotationDesc source) {
        Annotation annotation = new Annotation();
        annotation.setType(source.annotationType().qualifiedName());
        annotation.setName(source.annotationType().name());
        Map<String, Object> attribute = null;
        for (AnnotationDesc.ElementValuePair elementValuePair : source.elementValues()) {
            AnnotationTypeElementDoc element = elementValuePair.element();
            Object value = DocHelper.getValue(elementValuePair.value());
            if (attribute == null) {
                attribute = new HashMap<>(8);
            }
            attribute.put(element.name(), value == null ? DocHelper.getValue(element.defaultValue()) : value);
        }
        if (attribute != null) {
            annotation.setAttribute(attribute);
        }
        return Optional.of(annotation);
    }
}
