package ren.crux.rainbow.core.parser;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.Type;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.reader.parser.Context;

import java.util.Optional;

/**
 * 请求文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class DefaultRequestDocParser implements RequestDocParser {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    public boolean support(@NonNull Context context, @NonNull MethodDoc source) {
        return context.getRequestMappingDocParser().support(context, source);
    }

    @Override
    public Optional<Request> parse0(@NonNull Context context, @NonNull MethodDoc source) {
        log.debug("parse request : {}", source.name());
        Request request = new Request();
        context.getDescriptionDocParser().parse(context, source).ifPresent(request::setDesc);
        request.setAnnotations(context.getAnnotationDocParser().parse(context, source.annotations()));
        context.getRequestMappingDocParser().parse(context, source).ifPresent(request::setRequestMapping);
        request.setParams(context.getRequestParameterDocParser().parse(context, source.parameters()));
        Type type = source.returnType();
        request.setResponse(type.qualifiedTypeName());
        context.logType(type.qualifiedTypeName());
        return Optional.of(request);
    }
}
