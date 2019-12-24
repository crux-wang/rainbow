
package ren.crux.rainbow.core.module;

import com.sun.javadoc.*;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ModuleBuilder {

    private String name;
    private int order;
    private InterceptorBuilder<Pair<Class<?>, ClassDoc>, Entry> entryInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Pair<Field, FieldDoc>, EntryField> entryFieldInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Pair<Method, MethodDoc>, EntryMethod> entryMethodInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<java.lang.annotation.Annotation, Annotation> annotationInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Doc, CommentText> commentTextInterceptorBuilder = new InterceptorBuilder<>(this);

    private InterceptorBuilder<Pair<RequestGroup, ClassDoc>, RequestGroup> requestGroupInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Pair<Request, MethodDoc>, Request> requestInterceptorBuilder = new InterceptorBuilder<>(this);
    private InterceptorBuilder<Pair<RequestParam, ParamTag>, RequestParam> requestParamInterceptorBuilder = new InterceptorBuilder<>(this);

    public InterceptorBuilder<Pair<Class<?>, ClassDoc>, Entry> entry() {
        return entryInterceptorBuilder;
    }

    public InterceptorBuilder<Pair<Field, FieldDoc>, EntryField> entryField() {
        return entryFieldInterceptorBuilder;
    }

    public InterceptorBuilder<Pair<Method, MethodDoc>, EntryMethod> entryMethod() {
        return entryMethodInterceptorBuilder;
    }

    public InterceptorBuilder<java.lang.annotation.Annotation, Annotation> annotation() {
        return annotationInterceptorBuilder;
    }

    public InterceptorBuilder<Doc, CommentText> commentText() {
        return commentTextInterceptorBuilder;
    }

    public InterceptorBuilder<Pair<RequestGroup, ClassDoc>, RequestGroup> requestGroup() {
        return requestGroupInterceptorBuilder;
    }

    public InterceptorBuilder<Pair<Request, MethodDoc>, Request> request() {
        return requestInterceptorBuilder;
    }

    public InterceptorBuilder<Pair<RequestParam, ParamTag>, RequestParam> requestParam() {
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
}
