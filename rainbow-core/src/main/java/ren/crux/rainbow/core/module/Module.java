package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.raonbow.common.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * 模块
 *
 * @author wangzhihui
 */
public interface Module {

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<Class<?>, Entry>> entry() {
        return Optional.empty();
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<Field, EntryField>> entryField() {
        return Optional.empty();
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<Method, EntryMethod>> entryMethod() {
        return Optional.empty();
    }

    /**
     * 注解拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<java.lang.annotation.Annotation, Annotation>> annotation() {
        return Optional.empty();
    }

    /**
     * 注释拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<Doc, CommentText>> commentText() {
        return Optional.empty();
    }

    /**
     * 请求拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<Request, Request>> request() {
        return Optional.empty();
    }

    /**
     * 请求参数拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<RequestParam, RequestParam>> requestParam() {
        return Optional.empty();
    }

    /**
     * 请求组拦截器
     *
     * @return 拦截器构造器
     */
    default Optional<Interceptor<RequestGroup, RequestGroup>> requestGroup() {
        return Optional.empty();
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
    default int order() {
        return 0;
    }

    /**
     * 加载前初始化
     *
     * @param context 上下文
     */
    default void setUp(Context context) {
    }

    /**
     * 接口实现映射
     *
     * @return 接口实现映射
     */
    default Map<String, String> implMap() {
        return Collections.emptyMap();
    }
}
