package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ModuleImpl implements Module {

    private final String name;
    private final int order;
    private final CombinationInterceptor<Class<?>, Entry> clazzInterceptor;
    private final CombinationInterceptor<Field, EntryField> fieldInterceptor;
    private final CombinationInterceptor<Method, Request> methodInterceptor;
    private final CombinationInterceptor<Parameter, RequestParam> parameterInterceptor;
    private final CombinationInterceptor<java.lang.annotation.Annotation, Annotation> annotationInterceptor;
    private final CombinationInterceptor<Doc, CommentText> commentTextInterceptor;

    public ModuleImpl(String name, int order, CombinationInterceptor<Class<?>, Entry> clazzInterceptor, CombinationInterceptor<Field, EntryField> fieldInterceptor, CombinationInterceptor<Method, Request> methodInterceptor, CombinationInterceptor<Parameter, RequestParam> parameterInterceptor, CombinationInterceptor<java.lang.annotation.Annotation, Annotation> annotationInterceptor, CombinationInterceptor<Doc, CommentText> commentTextInterceptor) {
        this.name = name;
        this.order = order;
        this.clazzInterceptor = clazzInterceptor;
        this.fieldInterceptor = fieldInterceptor;
        this.methodInterceptor = methodInterceptor;
        this.parameterInterceptor = parameterInterceptor;
        this.annotationInterceptor = annotationInterceptor;
        this.commentTextInterceptor = commentTextInterceptor;
    }

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Class<?>, Entry> clazz() {
        return clazzInterceptor;
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Field, EntryField> field() {
        return fieldInterceptor;
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Method, Request> method() {
        return methodInterceptor;
    }

    /**
     * 参数拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public CombinationInterceptor<Parameter, RequestParam> parameter() {
        return parameterInterceptor;
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
