package ren.crux.rainbow.entry.parser.impl;

import com.sun.javadoc.Tag;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.entry.model.Ref;
import ren.crux.rainbow.entry.parser.RefDocParser;

import java.util.Optional;

public class RefParser implements RefDocParser {

    @Override
    public boolean support(@NonNull Context context, @NonNull Tag source) {
        return true;
    }

    @Override
    public Optional<Ref> parse(@NonNull Context context, @NonNull Tag source) {
        if ("Text".equals(source.name())) {
            return Optional.empty();
        }
        Ref ref = new Ref();
        ref.setName(source.name());
        ref.setText(source.text());
        context.findClass(source.text()).ifPresent(classDoc -> ref.setTarget(classDoc.qualifiedName()));
        return Optional.of(ref);
    }

}
