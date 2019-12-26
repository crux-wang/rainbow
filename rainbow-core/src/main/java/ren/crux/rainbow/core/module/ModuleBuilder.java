
package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModuleBuilder {

    private String name;
    private int order;
    private InterceptorBuilder<Class<?>, Entry> entryInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Field, EntryField> entryFieldInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Method, EntryMethod> entryMethodInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<java.lang.annotation.Annotation, Annotation> annotationInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Doc, CommentText> commentTextInterceptorBuilder = new InterceptorBuilder<>(this);

    private InterceptorBuilder<RequestGroup, RequestGroup> requestGroupInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Request, Request> requestInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<RequestParam, RequestParam> requestParamInterceptorBuilder = new InterceptorBuilder<>(this);

    public InterceptorBuilder<Class<?>, Entry> entry() {
        return entryInterceptorBuilder;
    }

    public InterceptorBuilder<Field, EntryField> entryField() {
        return entryFieldInterceptorBuilder;
    }

    public InterceptorBuilder<Method, EntryMethod> entryMethod() {
        return entryMethodInterceptorBuilder;
    }

    public InterceptorBuilder<java.lang.annotation.Annotation, Annotation> annotation() {
        return annotationInterceptorBuilder;
    }

    public InterceptorBuilder<Doc, CommentText> commentText() {
        return commentTextInterceptorBuilder;
    }

    public InterceptorBuilder<RequestGroup, RequestGroup> requestGroup() {
        return requestGroupInterceptorBuilder;
    }

    public InterceptorBuilder<Request, Request> request() {
        return requestInterceptorBuilder;
    }

    public InterceptorBuilder<RequestParam, RequestParam> requestParam() {
        return requestParamInterceptorBuilder;
    }

    /**
     * 设置模块名
     *
     * @param name 模块名
     * @return 自身
     */

    public ModuleBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * 设置加载序号
     *
     * @param order 序号
     * @return 自身
     */

    public ModuleBuilder order(int order) {
        this.order = order;
        return this;
    }

    /**
     * 构建
     *
     * @return 模块
     */

    public Module build() {
        return new ModuleImpl(name, order,
                entryInterceptorBuilder.build(),
                entryFieldInterceptorBuilder.build(),
                entryMethodInterceptorBuilder.build(),
                annotationInterceptorBuilder.build(),
                commentTextInterceptorBuilder.build(),
                requestGroupInterceptorBuilder.build(),
                requestInterceptorBuilder.build(),
                requestParamInterceptorBuilder.build()
        );
    }
//
//    public class EntryBuilder extends SubBuilder<ModuleBuilder> {
//
//        public EntryBuilder(ModuleBuilder superBuilder) {
//            super(superBuilder);
//        }
//
//        public EntryBuilder interceptor(Interceptor<Class<?>, Entry> interceptor) {
//            entryInterceptorBuilder.interceptor(interceptor);
//        }
//
//    }
}
