package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.javadoc.utils.JavaDocHelper;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * 注解解析器
 *
 * @author wangzhihui
 */
public class AnnotationParser extends AbstractEnhanceParser<Annotation, ren.crux.raonbow.common.model.Annotation> {

    public AnnotationParser() {
    }

    public AnnotationParser(Interceptor<Annotation, ren.crux.raonbow.common.model.Annotation> interceptor) {
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
    public Optional<ren.crux.raonbow.common.model.Annotation> parse0(Context context, Annotation source) {
        ren.crux.raonbow.common.model.Annotation annotationDetail = new ren.crux.raonbow.common.model.Annotation();
        Class<? extends Annotation> type = source.annotationType();
        annotationDetail.setName(type.getSimpleName());
        annotationDetail.setType(type.getCanonicalName());
        annotationDetail.setAttribute(JavaDocHelper.getAttributes(source));
        return Optional.of(annotationDetail);
    }
}
