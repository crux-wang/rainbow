package ren.crux.rainbow.request.parser;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import org.springframework.web.bind.annotation.RestController;
import ren.crux.rainbow.core.parser.ClassDocParser;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.request.model.RequestGroup;

import java.util.Arrays;

public interface RestControllerDocParser extends ClassDocParser<RequestGroup> {

    String REST_CONTROlLER_TYPE = RestController.class.getTypeName();

    @Override
    default boolean support(@NonNull Context context, @NonNull ClassDoc source) {
        return Arrays.stream(source.annotations()).anyMatch(a -> REST_CONTROlLER_TYPE.equals(a.annotationType().typeName()));
    }
}
