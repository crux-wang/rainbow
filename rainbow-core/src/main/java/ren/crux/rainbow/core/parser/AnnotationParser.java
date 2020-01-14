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
public class AnnotationParser extends AbstractEnhanceParser<Annotation, ren.crux.rainbow.core.model.Annotation> {

    public AnnotationParser() {
    }

    public AnnotationParser(Interceptor<Annotation, ren.crux.rainbow.core.model.Annotation> interceptor) {
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
    public Optional<ren.crux.rainbow.core.model.Annotation> parse0(Context context, Annotation source) {
        ren.crux.rainbow.core.model.Annotation annotationDetail = new ren.crux.rainbow.core.model.Annotation();
        Class<? extends Annotation> type = source.annotationType();
        annotationDetail.setName(type.getName());
        annotationDetail.setType(type.getCanonicalName());
        annotationDetail.setAttribute(JavaDocHelper.getAttributes(source));
        return Optional.of(annotationDetail);
    }
}
