package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.desc.model.CommentText;
import ren.crux.rainbow.core.desc.model.Describable;
import ren.crux.rainbow.core.desc.model.MethodDesc;
import ren.crux.rainbow.core.desc.model.ParameterDesc;
import ren.crux.rainbow.core.desc.reader.parser.CommonDocParser;
import ren.crux.rainbow.core.desc.reader.parser.Context;
import ren.crux.rainbow.core.desc.reader.parser.MethodDocParser;
import ren.crux.rainbow.core.desc.reader.parser.ParameterParser;

import java.util.Optional;
import java.util.stream.Collectors;

public class MethodDescParser implements MethodDocParser<MethodDesc> {

    public static final MethodDescParser INSTANCE = new MethodDescParser();

    private final CommonDocParser<Describable> descriptionDocParser = DescriptionDocParser.INSTANCE;
    private final ParameterParser<ParameterDesc> parameterDescParser = ParameterDescParser.INSTANCE;

    @Override
    public Optional<MethodDesc> parse(@NonNull Context context, @NonNull MethodDoc source) {
        if (context.getMethodDocFilter().doFilter(source)) {
            return descriptionDocParser.parse(context, source).map(desc -> {
                desc.setType(null);
                MethodDesc methodDesc = new MethodDesc();
                methodDesc.setDesc(desc);
                methodDesc.setParameters(parameterDescParser.parse(context, source.parameters()).stream().peek(parameterDesc -> {
                    Describable d = parameterDesc.getDesc();
                    desc.getTagRef("@param").ifPresent(tagRef -> {
                        d.setCommentText(new CommentText(StringUtils.trimToEmpty(StringUtils.removeStart(tagRef.getText(), d.getName()))));
                    });
                }).collect(Collectors.toList()));
                Describable returnType = new Describable();
                returnType.setName(source.returnType().simpleTypeName());
                returnType.setType(source.returnType().qualifiedTypeName());
                desc.getTagRef("@return").ifPresent(tagRef -> {
                    returnType.setCommentText(new CommentText(tagRef.getText()));
                });
                methodDesc.setReturnType(returnType);
                return methodDesc;
            });
        }
        return Optional.empty();
    }

}
