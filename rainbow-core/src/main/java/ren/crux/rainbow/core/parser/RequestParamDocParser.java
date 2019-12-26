package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.module.Context;

import java.util.Optional;

public class RequestParamDocParser extends AbstractEnhancer<RequestParam> {

    public RequestParamDocParser() {
    }

    public RequestParamDocParser(CombinationInterceptor<RequestParam, RequestParam> combinationInterceptor) {
        super(combinationInterceptor);
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
