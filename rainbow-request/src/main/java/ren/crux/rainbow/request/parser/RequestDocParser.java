package ren.crux.rainbow.request.parser;

import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import org.springframework.web.bind.annotation.*;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.MethodDocParser;
import ren.crux.rainbow.request.model.Request;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface RequestDocParser extends MethodDocParser<Request> {

    Set<String> MAPPING_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
            RequestMapping.class.getTypeName(),
            GetMapping.class.getTypeName(),
            PostMapping.class.getTypeName(),
            DeleteMapping.class.getTypeName(),
            PutMapping.class.getTypeName(),
            PatchMapping.class.getTypeName(),
            RequestMapping.class.getTypeName()
    )));

    @Override
    default boolean support(@NonNull Context context, @NonNull MethodDoc source) {
        return Arrays.stream(source.annotations()).anyMatch(a -> MAPPING_TYPES.contains(a.annotationType().typeName()));
    }

}
