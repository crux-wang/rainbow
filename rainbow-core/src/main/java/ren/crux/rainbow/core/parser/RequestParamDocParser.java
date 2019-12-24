package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ParamTag;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.module.Context;

import java.util.Optional;

public class RequestParamDocParser extends AbstractEnhanceParser<Pair<RequestParam, ParamTag>, RequestParam> {

    public RequestParamDocParser() {
    }

    public RequestParamDocParser(CombinationInterceptor<Pair<RequestParam, ParamTag>, RequestParam> combinationInterceptor) {
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
    protected Optional<RequestParam> parse0(Context context, Pair<RequestParam, ParamTag> source) {
        if (source == null || source.getLeft() == null) {
            return Optional.empty();
        }
        RequestParam requestParam = source.getLeft();
        ParamTag paramTag = source.getRight();
        if (paramTag != null) {
            requestParam.setCommentText(new CommentText(paramTag.parameterComment()));
        }
        return Optional.of(requestParam);
    }
}
