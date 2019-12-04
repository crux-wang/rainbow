package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.Parameter;
import lombok.NonNull;
import ren.crux.rainbow.core.desc.model.ParameterDesc;
import ren.crux.rainbow.core.desc.reader.parser.Context;
import ren.crux.rainbow.core.desc.reader.parser.ParameterParser;

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
