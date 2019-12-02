package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.MethodDoc;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import ren.crux.rainbow.core.model.RequestMapping;
import ren.crux.rainbow.core.parser.RequestMappingDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.utils.SpringWebRequestHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * spring web 请求映射文档解析器
 *
 * @author wangzhihui
 */
public class SpringWebRequestMappingDocParser implements RequestMappingDocParser {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    public boolean support(@NonNull Context context, @NonNull MethodDoc source) {
        return source.isPublic() && Arrays.stream(source.annotations()).anyMatch(a -> SpringWebRequestHelper.MAPPING_TYPES.contains(a.annotationType().qualifiedName()));
    }

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<List<RequestMapping>> parse0(@NonNull Context context, @NonNull MethodDoc source) {
        List<RequestMapping> allRequestMapping = SpringWebRequestHelper.getAllRequestMapping(source);
        if (CollectionUtils.isEmpty(allRequestMapping)) {
            return Optional.empty();
        }
        return Optional.of(allRequestMapping);
    }

}
