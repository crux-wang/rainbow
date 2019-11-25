package ren.crux.rainbow.request.parser.impl;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.parser.DescriptionDocParser;
import ren.crux.rainbow.entry.parser.impl.DescriptionParser;
import ren.crux.rainbow.request.model.RequestGroup;
import ren.crux.rainbow.request.parser.RestControllerDocParser;
import ren.crux.rainbow.request.utils.RequestHelper;

import java.util.Optional;

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
        RequestGroup group = new RequestGroup();
        group.setName(source.name());
        descriptionDocParser.parse(context, source).ifPresent(group::setDesc);
//        group.setPath();
        Optional<AnnotationDesc> optional = RequestHelper.getRequestMappingDesc(source);
        if (optional.isPresent()) {
            AnnotationDesc annotationDesc = optional.get();
            System.out.println(annotationDesc);
        }
        return Optional.empty();

    }
}
