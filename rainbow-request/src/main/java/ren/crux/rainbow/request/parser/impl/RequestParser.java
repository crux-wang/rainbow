package ren.crux.rainbow.request.parser.impl;

import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.AnnotationDocParser;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.impl.AnnotationParser;
import ren.crux.rainbow.entry.parser.impl.DescriptionParser;
import ren.crux.rainbow.request.RequestParameterDocParser;
import ren.crux.rainbow.request.model.Request;
import ren.crux.rainbow.request.model.RequestMapping;
import ren.crux.rainbow.request.model.RequestParam;
import ren.crux.rainbow.request.parser.RequestDocParser;
import ren.crux.rainbow.request.parser.RequestParameterParser;
import ren.crux.rainbow.request.utils.RequestHelper;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RequestParser implements RequestDocParser {

    private final DescriptionDocParser descriptionDocParser;
    private final AnnotationDocParser annotationDocParser;
    private final RequestParameterDocParser requestParameterDocParser;

    public RequestParser(DescriptionDocParser descriptionDocParser, AnnotationDocParser annotationDocParser, RequestParameterDocParser requestParameterDocParser) {
        this.descriptionDocParser = descriptionDocParser;
        this.annotationDocParser = annotationDocParser;
        this.requestParameterDocParser = requestParameterDocParser;
    }

    public RequestParser() {
        this.descriptionDocParser = new DescriptionParser();
        this.annotationDocParser = new AnnotationParser();
        this.requestParameterDocParser = new RequestParameterParser(annotationDocParser);
    }

    @Override
    public Optional<Request> parse(@NonNull Context context, @NonNull MethodDoc source) {
        log.debug("parse request : {}", source.name());
        Request request = new Request();
        descriptionDocParser.parse(context, source).ifPresent(request::setDesc);
        List<RequestMapping> allRequestMapping = RequestHelper.getAllRequestMapping(source);
        if (allRequestMapping.isEmpty()) {
            log.warn("ignored empty request mapping method! : {}", source.name());
            return Optional.empty();
        }
        request.setAnnotations(annotationDocParser.parse(context,source.annotations()));
        request.setParams(requestParameterDocParser.parse(context, source.parameters()));
        request.setRequestMapping(allRequestMapping);
        return Optional.of(request);
    }
}
