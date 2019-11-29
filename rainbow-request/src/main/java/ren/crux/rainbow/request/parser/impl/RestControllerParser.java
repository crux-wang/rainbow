package ren.crux.rainbow.request.parser.impl;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.impl.DescriptionParser;
import ren.crux.rainbow.request.model.Request;
import ren.crux.rainbow.request.model.RequestGroup;
import ren.crux.rainbow.request.parser.RequestDocParser;
import ren.crux.rainbow.request.parser.RestControllerDocParser;
import ren.crux.rainbow.request.utils.RequestHelper;

import java.util.List;
import java.util.Optional;

@Slf4j
public class RestControllerParser implements RestControllerDocParser {

    private final DescriptionDocParser descriptionDocParser;
    private final RequestDocParser requestDocParser;

    public RestControllerParser(DescriptionDocParser descriptionDocParser, RequestDocParser requestDocParser) {
        this.descriptionDocParser = descriptionDocParser;
        this.requestDocParser = requestDocParser;
    }

    public RestControllerParser() {
        this.descriptionDocParser = new DescriptionParser();
        this.requestDocParser = new RequestParser();
    }

    @Override
    public Optional<RequestGroup> parse(@NonNull Context context, @NonNull ClassDoc source) {
        log.debug("parse rest controller : {}", source.name());
        RequestGroup group = new RequestGroup();
        group.setName(source.name());
        descriptionDocParser.parse(context, source).ifPresent(group::setDesc);
        RequestHelper.getRequestMappingPath(source).ifPresent(group::setPath);
        List<Request> requests = requestDocParser.parse(context, source.methods(true));
        group.setRequests(requests);
        return Optional.of(group);
    }
}
