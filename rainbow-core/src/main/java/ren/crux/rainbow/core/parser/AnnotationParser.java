package ren.crux.rainbow.core.parser;

import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class AnnotationParser extends AbstractEnhanceParser<Annotation, ren.crux.rainbow.core.model.Annotation> {

    public static final AnnotationParser INSTANCE = new AnnotationParser();

    public AnnotationParser() {
    }

    public AnnotationParser(CombinationInterceptor<Annotation, ren.crux.rainbow.core.model.Annotation> combinationInterceptor) {
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
    public Optional<ren.crux.rainbow.core.model.Annotation> parse0(Context context, Annotation source) {
        return Optional.of(EntryUtils.process(source));
    }
}
