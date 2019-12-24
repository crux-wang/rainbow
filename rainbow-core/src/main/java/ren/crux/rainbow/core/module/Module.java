package ren.crux.rainbow.core.module;

import com.sun.javadoc.*;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

public interface Module {

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Pair<Class<?>, ClassDoc>, Entry> entry() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Pair<Field, FieldDoc>, EntryField> entryField() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Pair<Method, MethodDoc>, EntryMethod> entryMethod() {
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
     * 请求拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Pair<Request, MethodDoc>, Request> request() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 请求参数拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Pair<RequestParam, ParamTag>, RequestParam> requestParam() {
        return new CombinationInterceptor<>(Collections.emptyList());
    }

    /**
     * 请求组拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor<Pair<RequestGroup, ClassDoc>, RequestGroup> requestGroup() {
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
