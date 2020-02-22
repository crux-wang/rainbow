package ren.crux.rainbow.core.parser;

import org.apache.commons.collections4.CollectionUtils;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.module.Context;
import ren.crux.raonbow.common.model.Request;
import ren.crux.raonbow.common.model.RequestGroup;

import java.util.List;
import java.util.Optional;

/**
 * 请求组文档解析器
 *
 * @author wangzhihui
 */
public class RequestGroupDocParser extends AbstractEnhancer<RequestGroup> {

    private final CommentTextParser commentTextParser;
    private final RequestDocParser requestDocParser;

    public RequestGroupDocParser(CommentTextParser commentTextParser, RequestDocParser requestDocParser) {
        this.commentTextParser = commentTextParser;
        this.requestDocParser = requestDocParser;
    }

    public RequestGroupDocParser(Interceptor<RequestGroup, RequestGroup> interceptor, CommentTextParser commentTextParser, RequestDocParser requestDocParser) {
        super(interceptor);
        this.commentTextParser = commentTextParser;
        this.requestDocParser = requestDocParser;
    }

    /**
     * 内部解析方法
     *
     * @param context 上下文
     * @param source  源
     * @return 目标
     */
    @Override
    protected Optional<RequestGroup> parse0(Context context, RequestGroup source) {
        String type = source.getType();
        context.getClassDoc(type).ifPresent(classDoc -> {
            commentTextParser.parse(context, classDoc).ifPresent(source::setCommentText);
            List<Request> requests = source.getRequests();
            if (CollectionUtils.isNotEmpty(requests)) {
                // 添加方法文档缓存
                context.addPublicMethodDocs(classDoc);
                source.setRequests(requestDocParser.parse(context, requests));
            }
        });
        return Optional.of(source);
    }
}
