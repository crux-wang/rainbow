package ren.crux.rainbow.request.parser;

import com.sun.javadoc.Parameter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.model.Annotation;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;
import ren.crux.rainbow.entry.parser.impl.AnnotationParser;
import ren.crux.rainbow.request.RequestParameterDocParser;
import ren.crux.rainbow.request.model.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class RequestParameterParser implements RequestParameterDocParser {

    private final AnnotationDocParser annotationDocParser;

    public RequestParameterParser(AnnotationDocParser annotationDocParser) {
        this.annotationDocParser = annotationDocParser;
    }

    public RequestParameterParser() {
        this.annotationDocParser = new AnnotationParser();
    }

    @Override
    public boolean support(@NonNull Context context, @NonNull Parameter source) {
        return true;
    }

    @Override
    public Optional<RequestParam> parse(@NonNull Context context, @NonNull Parameter source) {
        log.debug("parse parameter : {}", source.name());
        RequestParam requestParam = new RequestParam();
        requestParam.setName(source.name());
        requestParam.setType(source.type().qualifiedTypeName());
        requestParam.setAnnotations(annotationDocParser.parse(context, source.annotations()));
        Map<String, Annotation> dict = requestParam.getAnnotations().stream().collect(Collectors.toMap(Annotation::getType, ann -> ann));
        Annotation annotation;
        annotation = dict.get(org.springframework.web.bind.annotation.RequestParam.class.getTypeName());
        if (annotation == null) {
            annotation = dict.get(org.springframework.web.bind.annotation.RequestHeader.class.getTypeName());
        }
        if (annotation == null) {
            annotation = dict.get(org.springframework.web.bind.annotation.RequestAttribute.class.getTypeName());
        }
        if (annotation == null) {
            annotation = dict.get(org.springframework.web.bind.annotation.PathVariable.class.getTypeName());
        }
        if (annotation == null) {
            annotation = dict.get(org.springframework.web.bind.annotation.CookieValue.class.getTypeName());
        }
        if (annotation == null) {
            annotation = dict.get(org.springframework.web.bind.annotation.SessionAttribute.class.getTypeName());
        }
        if (annotation == null) {
            annotation = dict.get(org.springframework.web.bind.annotation.RequestBody.class.getTypeName());
        }
        if (annotation == null) {
            requestParam.setRequired(false);
        } else {
            requestParam.setRequestType(annotation.getType());
            Map<String, Object> attribute = annotation.getAttribute();
            if (attribute == null) {
                requestParam.setRequired(false);
            } else {
                Boolean required = MapUtils.getBoolean(attribute, "required");
                String name = MapUtils.getString(attribute, "name");
                String defaultValue = MapUtils.getString(attribute, "defaultValue");
                if (required == null) {
                    requestParam.setRequired(true);
                } else {
                    requestParam.setRequired(required);
                }
                if (StringUtils.isNotBlank(name)) {
                    requestParam.setName(name);
                }
                if (StringUtils.isNotBlank(defaultValue)) {
                    requestParam.setDefaultValue(defaultValue);
                }
            }
        }

        return Optional.of(requestParam);
    }
}
