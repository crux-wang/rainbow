package ren.crux.rainbow.core.parser;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.CommentText;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.module.Context;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class RequestDocParser extends AbstractEnhanceParser<Pair<Request, MethodDoc>, Request> {

    private final CommentTextParser commentTextParser;
    private final RequestParamDocParser requestParamDocParser;

    public RequestDocParser(CombinationInterceptor<Pair<Request, MethodDoc>, Request> combinationInterceptor, CommentTextParser commentTextParser, RequestParamDocParser requestParamDocParser) {
        super(combinationInterceptor);
        this.commentTextParser = commentTextParser;
        this.requestParamDocParser = requestParamDocParser;
    }

    public RequestDocParser(CommentTextParser commentTextParser, RequestParamDocParser requestParamDocParser) {
        this.commentTextParser = commentTextParser;
        this.requestParamDocParser = requestParamDocParser;
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<Request> parse0(Context context, Pair<Request, MethodDoc> source) {
        if (source == null || source.getLeft() == null) {
            return Optional.empty();
        }
        Request request = source.getLeft();
        MethodDoc methodDoc = source.getRight();
        if (methodDoc == null) {
            return Optional.of(request);
        }
        commentTextParser.parse(context, methodDoc).ifPresent(request::setCommentText);
        List<RequestParam> params = request.getParams();
        if (CollectionUtils.isNotEmpty(params)) {
            if (params.size() != methodDoc.paramTags().length) {
                log.warn("param tag size not match! , ignored method : {}", request.getSignature());
            }
            List<RequestParam> requestParams = requestParamDocParser.parse(context, buildRequestParamMethodDocPairs(methodDoc, params));
            request.setParams(requestParams);
        }
        Tag[] tags = methodDoc.tags("@return");
        if (tags.length > 0) {
            request.setReturnCommentText(tags[0].text());
        }
        CommentText commentText = request.getCommentText();
        // 删除参数标签
        commentText.setTags(commentText.getTags().stream().filter(t -> !StringUtils.equals("@param", t.getName())).collect(Collectors.toList()));
        return Optional.of(request);
    }

    @SuppressWarnings("unchecked")
    private Pair<RequestParam, ParamTag>[] buildRequestParamMethodDocPairs(MethodDoc methodDoc, List<RequestParam> params) {
        ParamTag[] paramTags = methodDoc.paramTags();
        Pair<RequestParam, ParamTag>[] pairs = new Pair[params.size()];
        for (int i = 0; i < params.size(); i++) {
            RequestParam param = params.get(i);
            ParamTag paramTag = paramTags[i];
            param.setName(paramTag.name());
            pairs[i] = Pair.of(param, paramTag);
        }
        return pairs;
    }

}
