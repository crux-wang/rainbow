package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.interceptor.CombinationInterceptor;
import ren.crux.rainbow.core.model.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 模块
 *
 * @author wangzhihui
 */
public interface ModuleBuilder {

    /**
     * 配置类拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor.CombinationInterceptorBuilder<Class<?>, Entry> clazz() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置属性拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor.CombinationInterceptorBuilder<Field, EntryField> field() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置方法拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor.CombinationInterceptorBuilder<Method, Request> method() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置参数拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor.CombinationInterceptorBuilder<Parameter, RequestParam> parameter() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置注解拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor.CombinationInterceptorBuilder<java.lang.annotation.Annotation, Annotation> annotation() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置注释拦截器
     *
     * @return 拦截器构造器
     */
    default CombinationInterceptor.CombinationInterceptorBuilder<Doc, CommentText> commentText() {
        return CombinationInterceptor.builder();
    }

    /**
     * 设置模块名
     *
     * @param name 模块名
     * @return 自身
     */
    ModuleBuilder name(String name);

    /**
     * 设置加载序号
     *
     * @param order 序号
     * @return 自身
     */
    ModuleBuilder order(int order);

    /**
     * 构建
     *
     * @return 模块
     */
    Module build();

}
