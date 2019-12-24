package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.module.Context;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RequestGroupDocParser extends AbstractEnhanceParser<Pair<RequestGroup, ClassDoc>, RequestGroup> {

    private final CommentTextParser commentTextParser;
    private final RequestDocParser requestDocParser;

    public RequestGroupDocParser(CommentTextParser commentTextParser, RequestDocParser requestDocParser) {
        this.commentTextParser = commentTextParser;
        this.requestDocParser = requestDocParser;
    }

    public RequestGroupDocParser(CombinationInterceptor<Pair<RequestGroup, ClassDoc>, RequestGroup> combinationInterceptor, CommentTextParser commentTextParser, RequestDocParser requestDocParser) {
        super(combinationInterceptor);
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
    protected Optional<RequestGroup> parse0(Context context, Pair<RequestGroup, ClassDoc> source) {
        if (source == null || source.getLeft() == null) {
            return Optional.empty();
        }
        RequestGroup requestGroup = source.getLeft();
        ClassDoc classDoc = source.getRight();
        if (classDoc == null) {
            return Optional.of(requestGroup);
        }
        commentTextParser.parse(context, classDoc).ifPresent(requestGroup::setCommentText);
        List<Request> requests = requestGroup.getRequests();
        if (CollectionUtils.isNotEmpty(requests)) {
            requestDocParser.parse(context, buildRequestMethodDocPairs(context, classDoc, requests));
        }
        return Optional.of(requestGroup);
    }

    @SuppressWarnings("unchecked")
    private Pair<Request, MethodDoc>[] buildRequestMethodDocPairs(Context context, ClassDoc classDoc, List<Request> requests) {
        Map<String, MethodDoc> publicMethodDocs = context.getPublicMethodDocs(classDoc);
        return requests.stream()
                .map(r -> Pair.of(r, publicMethodDocs.get(r.getSignature()))).toArray(Pair[]::new);
    }
}
