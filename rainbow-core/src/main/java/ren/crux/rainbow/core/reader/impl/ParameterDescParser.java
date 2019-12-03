package ren.crux.rainbow.core.reader.impl;

import com.sun.javadoc.Parameter;
import lombok.NonNull;
import ren.crux.rainbow.core.model.Describable;
import ren.crux.rainbow.core.model.ParameterDesc;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.reader.parser.ParameterParser;

import java.util.Optional;

public class ParameterDescParser implements ParameterParser<ParameterDesc> {

    public static final ParameterDescParser INSTANCE = new ParameterDescParser();

    @Override
    public Optional<ParameterDesc> parse(@NonNull Context context, @NonNull Parameter source) {
        ParameterDesc parameterDesc = new ParameterDesc();
        Describable desc = new Describable();
        desc.setName(source.name());
        desc.setType(source.type().qualifiedTypeName());
        parameterDesc.setDesc(desc);
        return Optional.of(parameterDesc);
    }
}
