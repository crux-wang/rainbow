package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.Tag;
import lombok.NonNull;
import ren.crux.rainbow.core.desc.model.TagRef;
import ren.crux.rainbow.core.desc.reader.parser.Context;
import ren.crux.rainbow.core.desc.reader.parser.TagParser;

import java.util.Optional;

public class TagRefParser implements TagParser<TagRef> {

    public static final TagRefParser INSTANCE = new TagRefParser();

    @Override
    public Optional<TagRef> parse(@NonNull Context context, @NonNull Tag source) {
        if ("Text".equals(source.name())) {
            return Optional.empty();
        }
        TagRef ref = new TagRef();
        ref.setName(source.name());
        ref.setText(source.text());
        context.findClass(source.text()).ifPresent(classDoc -> ref.setTarget(classDoc.qualifiedName()));
        return Optional.of(ref);
    }

}
