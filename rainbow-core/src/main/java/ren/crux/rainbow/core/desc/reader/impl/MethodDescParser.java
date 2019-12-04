package ren.crux.rainbow.core.desc.reader.impl;

import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.desc.model.ClassDesc;
import ren.crux.rainbow.core.desc.model.CommentText;
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

    private final CommonDocParser<CommentText> descriptionDocParser = CommentTextParser.INSTANCE;
    private final ParameterParser<ParameterDesc> parameterDescParser = ParameterDescParser.INSTANCE;

    @Override
    public Optional<MethodDesc> parse(@NonNull Context context, @NonNull MethodDoc source) {
        if (context.getMethodDocFilter().doFilter(source)) {
            return descriptionDocParser.parse(context, source).map(commentText -> {
                MethodDesc methodDesc = new MethodDesc();
                methodDesc.setName(source.name());
                methodDesc.setType(source.qualifiedName());
                methodDesc.setCommentText(commentText);
                methodDesc.setParameters(parameterDescParser.parse(context, source.parameters()).stream().peek(parameterDesc -> {
                    commentText.getTagRef("@param").ifPresent(tagRef -> {
                        parameterDesc.setCommentText(new CommentText(StringUtils.trimToEmpty(StringUtils.removeStart(tagRef.getText(), parameterDesc.getName()))));
                    });
                }).collect(Collectors.toList()));
                ClassDesc returnType = new ClassDesc();
                returnType.setName(source.returnType().simpleTypeName());
                returnType.setType(source.returnType().qualifiedTypeName());
                commentText.getTagRef("@return").ifPresent(tagRef -> {
                    methodDesc.setCommentText(new CommentText(tagRef.getText()));
                });
                methodDesc.setReturnType(returnType);
                return methodDesc;
            });
        }
        return Optional.empty();
    }

}
