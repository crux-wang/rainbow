package ren.crux.rainbow.core.parser;

import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.module.Context;
import ren.crux.raonbow.common.model.Request;
import ren.crux.raonbow.common.model.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 请求文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class RequestDocParser extends AbstractEnhancer<Request> {

    private final CommentTextParser commentTextParser;
    private final RequestParamDocParser requestParamDocParser;

    public RequestDocParser(CommentTextParser commentTextParser, RequestParamDocParser requestParamDocParser) {
        this.commentTextParser = commentTextParser;
        this.requestParamDocParser = requestParamDocParser;
    }

    public RequestDocParser(Interceptor<Request, Request> interceptor, CommentTextParser commentTextParser, RequestParamDocParser requestParamDocParser) {
        super(interceptor);
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
    protected Optional<Request> parse0(Context context, Request source) {
        String signature = source.getSignature();
        context.getPublicMethodDoc(signature).ifPresent(methodDoc -> {
            commentTextParser.parse(context, methodDoc).ifPresent(source::setCommentText);
            List<RequestParam> params = source.getParams();
            if (CollectionUtils.isNotEmpty(params)) {
                if (params.size() != methodDoc.paramTags().length) {
                    log.warn("param tag size not match! , ignored method : {}", source.getSignature());
                } else {
                    // 设置参数标签缓存
                    context.addParamTags(source.getSignature(), buildParamMap(methodDoc, params));
                    List<RequestParam> requestParams = requestParamDocParser.parse(context, params);
                    source.setParams(requestParams);
                }
            }
            Tag[] tags = methodDoc.tags("@return");
            if (tags.length > 0) {
                source.setReturnCommentText(tags[0].text());
            }
        });
        return Optional.of(source);
    }

    private Map<String, ParamTag> buildParamMap(MethodDoc methodDoc, List<RequestParam> params) {
        Map<String, ParamTag> map = new HashMap<>(params.size());
        ParamTag[] paramTags = methodDoc.paramTags();
        for (int i = 0; i < params.size(); i++) {
            RequestParam param = params.get(i);
            ParamTag paramTag = paramTags[i];
            param.setName(paramTag.parameterName());
            map.put(param.getName(), paramTag);
        }
        return map;
    }
}
