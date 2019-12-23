package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;

public interface Module {

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Class<?>, Entry> clazz() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Field, EntryField> field() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Method, Request> method() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 参数拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Parameter, RequestParam> parameter() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 注解拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<java.lang.annotation.Annotation, Annotation> annotation() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 注释拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Doc, CommentText> commentText() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 模块名
     *
     * @return 模块名
     */
    String name();

    /**
     * 加载序号
     *
     * @return 序号
     */
    int order();

}
