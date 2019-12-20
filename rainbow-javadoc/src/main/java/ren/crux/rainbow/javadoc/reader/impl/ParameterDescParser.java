package ren.crux.rainbow.javadoc.reader.impl;

import com.sun.javadoc.Parameter;
import lombok.NonNull;
import ren.crux.rainbow.javadoc.model.ParameterDesc;
import ren.crux.rainbow.javadoc.reader.parser.Context;
import ren.crux.rainbow.javadoc.reader.parser.ParameterParser;

import java.util.Optional;

public class ParameterDescParser implements ParameterParser<ParameterDesc> {

    public static final ParameterDescParser INSTANCE = new ParameterDescParser();

    @Override
    public Optional<ParameterDesc> parse(@NonNull Context context, @NonNull Parameter source) {
        ParameterDesc parameterDesc = new ParameterDesc();
        parameterDesc.setName(source.name());
        parameterDesc.setType(source.type().qualifiedTypeName());
        return Optional.of(parameterDesc);
    }
}
