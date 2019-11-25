package ren.crux.rainbow.request.parser.impl;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.impl.DescriptionParser;
import ren.crux.rainbow.request.model.RequestGroup;
import ren.crux.rainbow.request.parser.RestControllerDocParser;
import ren.crux.rainbow.request.utils.RequestHelper;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class RestControllerParser implements RestControllerDocParser {

    private final DescriptionDocParser descriptionDocParser;

    public RestControllerParser(DescriptionDocParser descriptionDocParser) {
        this.descriptionDocParser = descriptionDocParser;
    }

    public RestControllerParser() {
        this.descriptionDocParser = new DescriptionParser();
    }

    @Override
    public Optional<RequestGroup> parse(@NonNull Context context, @NonNull ClassDoc source) {
        System.out.println("parse rest controller : " + source.name());
        RequestGroup group = new RequestGroup();
        group.setName(source.name());
        descriptionDocParser.parse(context, source).ifPresent(group::setDesc);
        RequestHelper.getRequestMappingPath(source).ifPresent(group::setPath);
        return Optional.of(group);
    }
}
