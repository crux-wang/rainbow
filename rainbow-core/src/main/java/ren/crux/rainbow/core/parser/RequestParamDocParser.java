package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.module.Context;
import ren.crux.raonbow.common.model.CommentText;
import ren.crux.raonbow.common.model.RequestParam;

import java.util.Optional;

/**
 * 请求参数文档解析器
 *
 * @author wangzhihui
 */
public class RequestParamDocParser extends AbstractEnhancer<RequestParam> {

    public RequestParamDocParser() {
    }

    public RequestParamDocParser(Interceptor<RequestParam, RequestParam> interceptor) {
        super(interceptor);
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<RequestParam> parse0(Context context, RequestParam source) {
        context.getParamTag(source.getDeclaringSignature(), source.getName()).ifPresent(paramTag -> {
            source.setCommentText(new CommentText(paramTag.parameterComment()));
        });
        return Optional.of(source);
    }
}
