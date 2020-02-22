package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import lombok.NonNull;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.raonbow.common.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wangzhihui
 */
public class CombinationModule implements Module {
    /**
     * 模块列表
     */
    private final List<Module> modules;

    private CombinationInterceptor<Class<?>, Entry> entryCombinationInterceptor;
    private CombinationInterceptor<Field, EntryField> entryFieldCombinationInterceptor;
    private CombinationInterceptor<Method, EntryMethod> entryMethodCombinationInterceptor;
    private CombinationInterceptor<Annotation, ren.crux.raonbow.common.model.Annotation> annotationAnnotationCombinationInterceptor;
    private CombinationInterceptor<Doc, CommentText> commentTextCombinationInterceptor;
    private CombinationInterceptor<Request, Request> requestCombinationInterceptor;
    private CombinationInterceptor<RequestParam, RequestParam> requestParamCombinationInterceptor;
    private CombinationInterceptor<RequestGroup, RequestGroup> requestGroupCombinationInterceptor;

    private Map<String, String> implMap = new HashMap<>();

    public CombinationModule(@NonNull List<Module> modules) {
        this.modules = modules;
    }

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Class<?>, Entry>> entry() {
        return Optional.ofNullable(entryCombinationInterceptor);
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Field, EntryField>> entryField() {
        return Optional.ofNullable(entryFieldCombinationInterceptor);
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Method, EntryMethod>> entryMethod() {
        return Optional.ofNullable(entryMethodCombinationInterceptor);
    }

    /**
     * 注解拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Annotation, ren.crux.raonbow.common.model.Annotation>> annotation() {
        return Optional.ofNullable(annotationAnnotationCombinationInterceptor);
    }

    /**
     * 注释拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Doc, CommentText>> commentText() {
        return Optional.ofNullable(commentTextCombinationInterceptor);
    }

    /**
     * 请求拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Request, Request>> request() {
        return Optional.ofNullable(requestCombinationInterceptor);
    }

    /**
     * 请求参数拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<RequestParam, RequestParam>> requestParam() {
        return Optional.ofNullable(requestParamCombinationInterceptor);
    }

    /**
     * 请求组拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<RequestGroup, RequestGroup>> requestGroup() {
        return Optional.ofNullable(requestGroupCombinationInterceptor);
    }

    /**
     * 模块名
     *
     * @return 模块名
     */
    @Override
    public String name() {
        return "$" + this.getClass().getSimpleName();
    }

    /**
     * 加载序号
     *
     * @return 序号
     */
    @Override
    public int order() {
        return 0;
    }

    /**
     * 加载前初始化
     *
     * @param context 上下文
     */
    @Override
    public void setUp(Context context) {
        CombinationInterceptor.CombinationInterceptorBuilder<Class<?>, Entry> entryCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<Field, EntryField> entryFieldCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<Method, EntryMethod> entryMethodCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<Annotation, ren.crux.raonbow.common.model.Annotation> annotationAnnotationCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<Doc, CommentText> commentTextCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<Request, Request> requestCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<RequestParam, RequestParam> requestParamCombinationInterceptorBuilder = CombinationInterceptor.builder();
        CombinationInterceptor.CombinationInterceptorBuilder<RequestGroup, RequestGroup> requestGroupCombinationInterceptorBuilder = CombinationInterceptor.builder();
        for (Module module : modules) {
            module.setUp(context);
            module.entry().ifPresent(entryCombinationInterceptorBuilder::interceptor);
            module.entryField().ifPresent(entryFieldCombinationInterceptorBuilder::interceptor);
            module.entryMethod().ifPresent(entryMethodCombinationInterceptorBuilder::interceptor);
            module.annotation().ifPresent(annotationAnnotationCombinationInterceptorBuilder::interceptor);
            module.commentText().ifPresent(commentTextCombinationInterceptorBuilder::interceptor);
            module.request().ifPresent(requestCombinationInterceptorBuilder::interceptor);
            module.requestParam().ifPresent(requestParamCombinationInterceptorBuilder::interceptor);
            module.requestGroup().ifPresent(requestGroupCombinationInterceptorBuilder::interceptor);
            implMap.putAll(module.implMap());
        }
        entryCombinationInterceptorBuilder.build().ifPresent(this::setEntryCombinationInterceptor);
        entryFieldCombinationInterceptorBuilder.build().ifPresent(this::setEntryFieldCombinationInterceptor);
        entryMethodCombinationInterceptorBuilder.build().ifPresent(this::setEntryMethodCombinationInterceptor);
        annotationAnnotationCombinationInterceptorBuilder.build().ifPresent(this::setAnnotationAnnotationCombinationInterceptor);
        commentTextCombinationInterceptorBuilder.build().ifPresent(this::setCommentTextCombinationInterceptor);
        requestCombinationInterceptorBuilder.build().ifPresent(this::setRequestCombinationInterceptor);
        requestParamCombinationInterceptorBuilder.build().ifPresent(this::setRequestParamCombinationInterceptor);
        requestGroupCombinationInterceptorBuilder.build().ifPresent(this::setRequestGroupCombinationInterceptor);
    }

    private void setEntryCombinationInterceptor(CombinationInterceptor<Class<?>, Entry> entryCombinationInterceptor) {
        this.entryCombinationInterceptor = entryCombinationInterceptor;
    }

    private void setEntryFieldCombinationInterceptor(CombinationInterceptor<Field, EntryField> entryFieldCombinationInterceptor) {
        this.entryFieldCombinationInterceptor = entryFieldCombinationInterceptor;
    }

    private void setEntryMethodCombinationInterceptor(CombinationInterceptor<Method, EntryMethod> entryMethodCombinationInterceptor) {
        this.entryMethodCombinationInterceptor = entryMethodCombinationInterceptor;
    }

    private void setAnnotationAnnotationCombinationInterceptor(CombinationInterceptor<Annotation, ren.crux.raonbow.common.model.Annotation> annotationAnnotationCombinationInterceptor) {
        this.annotationAnnotationCombinationInterceptor = annotationAnnotationCombinationInterceptor;
    }

    private void setCommentTextCombinationInterceptor(CombinationInterceptor<Doc, CommentText> commentTextCombinationInterceptor) {
        this.commentTextCombinationInterceptor = commentTextCombinationInterceptor;
    }

    private void setRequestCombinationInterceptor(CombinationInterceptor<Request, Request> requestCombinationInterceptor) {
        this.requestCombinationInterceptor = requestCombinationInterceptor;
    }

    private void setRequestParamCombinationInterceptor(CombinationInterceptor<RequestParam, RequestParam> requestParamCombinationInterceptor) {
        this.requestParamCombinationInterceptor = requestParamCombinationInterceptor;
    }

    private void setRequestGroupCombinationInterceptor(CombinationInterceptor<RequestGroup, RequestGroup> requestGroupCombinationInterceptor) {
        this.requestGroupCombinationInterceptor = requestGroupCombinationInterceptor;
    }

    /**
     * 接口实现映射
     *
     * @return 接口实现映射
     */
    @Override
    public Map<String, String> implMap() {
        return implMap;
    }
}
