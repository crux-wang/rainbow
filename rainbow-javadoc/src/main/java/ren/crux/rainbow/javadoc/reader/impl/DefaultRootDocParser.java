package ren.crux.rainbow.javadoc.reader.impl;

import com.sun.javadoc.RootDoc;
import lombok.NonNull;
import ren.crux.rainbow.javadoc.model.ClassDesc;
import ren.crux.rainbow.javadoc.reader.parser.ClassDocParser;
import ren.crux.rainbow.javadoc.reader.parser.Context;
import ren.crux.rainbow.javadoc.reader.parser.RootDocParser;

import java.util.List;
import java.util.Optional;

public class DefaultRootDocParser implements RootDocParser<List<ClassDesc>> {

    private final ClassDocParser<ClassDesc> classDescParser = ClassDescParser.INSTANCE;

    @Override
    public Optional<List<ClassDesc>> parse(@NonNull Context context, @NonNull RootDoc source) {
        return Optional.of(classDescParser.parse(context, source.classes()));
    }

    @Override
    public List<List<ClassDesc>> parse(@NonNull Context context, @NonNull RootDoc[] source) {
        throw new UnsupportedOperationException();
    }
}
