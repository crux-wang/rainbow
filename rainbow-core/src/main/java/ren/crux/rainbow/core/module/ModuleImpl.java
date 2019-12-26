package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModuleImpl implements Module {

    private final String name;
    private final int order;
    private final CombinationInterceptor<Class<?>, Entry> clazzInterceptor;
    private final CombinationInterceptor<Field, EntryField> fieldInterceptor;
    private final CombinationInterceptor<Method, EntryMethod> methodInterceptor;
    private final CombinationInterceptor<java.lang.annotation.Annotation, Annotation> annotationInterceptor;
    private final CombinationInterceptor<Doc, CommentText> commentTextInterceptor;
    private CombinationInterceptor<RequestGroup, RequestGroup> requestGroupInterceptor;
    private CombinationInterceptor<Request, Request> requestInterceptor;
    private CombinationInterceptor<RequestParam, RequestParam> requestParamInterceptor;

    public ModuleImpl(String name, int order, CombinationInterceptor<Class<?>, Entry> clazzInterceptor, CombinationInterceptor<Field, EntryField> fieldInterceptor, CombinationInterceptor<Method, EntryMethod> methodInterceptor, CombinationInterceptor<java.lang.annotation.Annotation, Annotation> annotationInterceptor, CombinationInterceptor<Doc, CommentText> commentTextInterceptor, CombinationInterceptor<RequestGroup, RequestGroup> requestGroupInterceptor, CombinationInterceptor<Request, Request> requestInterceptor, CombinationInterceptor<RequestParam, RequestParam> requestParamInterceptor) {
        this.name = name;
        this.order = order;
        this.clazzInterceptor = clazzInterceptor;
        this.fieldInterceptor = fieldInterceptor;
        this.methodInterceptor = methodInterceptor;
        this.annotationInterceptor = annotationInterceptor;
        this.commentTextInterceptor = commentTextInterceptor;
        this.requestGroupInterceptor = requestGroupInterceptor;
        this.requestInterceptor = requestInterceptor;
        this.requestParamInterceptor = requestParamInterceptor;
    }

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Class<?>, Entry> entry() {
        return clazzInterceptor;
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Field, EntryField> entryField() {
        return fieldInterceptor;
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Method, EntryMethod> entryMethod() {
        return methodInterceptor;
    }

    /**
     * 注解拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<java.lang.annotation.Annotation, Annotation> annotation() {
        return annotationInterceptor;
    }

    /**
     * 注释拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Doc, CommentText> commentText() {
        return commentTextInterceptor;
    }


    /**
     * 请求拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Request, Request> request() {
        return requestInterceptor;
    }

    /**
     * 请求参数拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<RequestParam, RequestParam> requestParam() {
        return requestParamInterceptor;
    }

    /**
     * 请求组拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<RequestGroup, RequestGroup> requestGroup() {
        return requestGroupInterceptor;
    }

    /**
     * 模块名
     *
     * @return 模块名
     */
    @Override
    public String name() {
        return name;
    }

    /**
     * 加载序号
     *
     * @return 序号
     */
    @Override
    public int order() {
        return order;
    }
}
