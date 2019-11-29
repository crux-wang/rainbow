package ren.crux.rainbow.request.parser;

import com.sun.javadoc.Parameter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.model.Annotation;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;
import ren.crux.rainbow.entry.parser.impl.AnnotationParser;
import ren.crux.rainbow.request.RequestParameterDocParser;
import ren.crux.rainbow.request.model.RequestParam;
import ren.crux.rainbow.request.utils.RequestHelper;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RequestParameterParser implements RequestParameterDocParser {

    private final AnnotationDocParser annotationDocParser;

    public RequestParameterParser(AnnotationDocParser annotationDocParser) {
        this.annotationDocParser = annotationDocParser;
    }

    public RequestParameterParser() {
        this.annotationDocParser=new AnnotationParser();
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
        // ...
        return Optional.of(requestParam);
    }
}
