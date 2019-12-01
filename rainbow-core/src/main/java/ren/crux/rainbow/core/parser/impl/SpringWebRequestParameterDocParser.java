package ren.crux.rainbow.core.parser.impl;

import com.sun.javadoc.Parameter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.Annotation;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.model.RequestType;
import ren.crux.rainbow.core.parser.RequestParameterDocParser;
import ren.crux.rainbow.core.reader.parser.Context;
import ren.crux.rainbow.core.utils.SpringWebRequestHelper;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
@Slf4j
public class SpringWebRequestParameterDocParser implements RequestParameterDocParser {

    /**
     * 支持条件
     *
     * @param context 上下文
     * @param source  解析源
     * @return 是否支持
     */
    @Override
    public boolean support(@NonNull Context context, @NonNull Parameter source) {
        return true;
    }

    /**
     * 解析
     *
     * @param context 上下文
     * @param source  解析源
     * @return 解析后的产物
     */
    @Override
    public Optional<RequestParam> parse(@NonNull Context context, @NonNull Parameter source) {
        log.debug("parse parameter : {}", source.name());
        RequestParam requestParam = new RequestParam();
        requestParam.setName(source.name());
        requestParam.setType(source.type().qualifiedTypeName());
        requestParam.setAnnotations(context.getAnnotationDocParser().parse(context, source.annotations()));
        Map<String, Annotation> dict = requestParam.getAnnotations().stream().collect(Collectors.toMap(Annotation::getType, ann -> ann));
        Annotation annotation;
        RequestType requestType = RequestType.request_param;
        annotation = dict.get(SpringWebRequestHelper.REQUEST_PARAM_TYPE);
        if (annotation == null) {
            requestType = RequestType.request_header;
            annotation = dict.get(SpringWebRequestHelper.REQUEST_HEADER_TYPE);
        }
        if (annotation == null) {
            requestType = RequestType.request_attribute;
            annotation = dict.get(SpringWebRequestHelper.REQUEST_ATTRIBUTE_TYPE);
        }
        if (annotation == null) {
            requestType = RequestType.path_variable;
            annotation = dict.get(SpringWebRequestHelper.PATH_VARIABLE_TYPE);
        }
        if (annotation == null) {
            requestType = RequestType.cookie_value;
            annotation = dict.get(SpringWebRequestHelper.COOKIE_VALUE_TYPE);
        }
        if (annotation == null) {
            requestType = RequestType.session_attribute;
            annotation = dict.get(SpringWebRequestHelper.SESSION_ATTRIBUTE_TYPE);
        }
        if (annotation == null) {
            requestType = RequestType.request_body;
            annotation = dict.get(SpringWebRequestHelper.REQUEST_BODY_TYPE);
        }
        if (annotation == null) {
            requestType = RequestType.request_param;
            requestParam.setRequired(false);
        } else {
            Map<String, Object> attribute = annotation.getAttribute();
            if (attribute == null) {
                requestParam.setRequired(false);
            } else {
                Boolean required = MapUtils.getBoolean(attribute, "required");
                String name = MapUtils.getString(attribute, "name");
                String defaultValue = MapUtils.getString(attribute, "defaultValue");
                if (required == null) {
                    requestParam.setRequired(true);
                } else {
                    requestParam.setRequired(required);
                }
                if (StringUtils.isNotBlank(name)) {
                    requestParam.setName(name);
                }
                if (StringUtils.isNotBlank(defaultValue)) {
                    requestParam.setDefaultValue(defaultValue);
                }
            }
        }
        requestParam.setRequestType(requestType);
        return Optional.of(requestParam);
    }
}
