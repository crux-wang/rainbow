package ren.crux.rainbow.core.parser;

import com.sun.javadoc.ClassDoc;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.utils.SpringWebRequestHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * spring web 请求组文档解析器
 *
 * @author wangzhihui
 */
@Slf4j
public class SpringWebRequestGroupDocParser implements RequestGroupDocParser {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    public boolean support(@NonNull Context context, @NonNull ClassDoc source) {
        return Arrays.stream(source.annotations()).anyMatch(a -> SpringWebRequestHelper.REST_CONTROlLER_TYPE.equals(a.annotationType().qualifiedName()));
    }

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<RequestGroup> parse(@NonNull Context context, @NonNull ClassDoc source) {
        log.debug("parse rest controller : {}", source.name());
        RequestGroup group = new RequestGroup();
        group.setName(source.name());
        context.getDescriptionDocParser().parse(context, source).ifPresent(group::setDesc);
        SpringWebRequestHelper.getRequestMappingPath(source).ifPresent(group::setPath);
        List<Request> requests = context.getRequestDocParser().parse(context, source.methods(true));
        group.setRequests(requests);
        return Optional.of(group);
    }
}
