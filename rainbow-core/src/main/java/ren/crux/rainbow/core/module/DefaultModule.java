package ren.crux.rainbow.core.module;

import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.EntryField;
import ren.crux.rainbow.core.model.EntryMethod;
import ren.crux.rainbow.core.model.RequestParam;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 默认模块
 *
 * @author wangzhihui
 */
public class DefaultModule implements Module {

    public static final DefaultModule INSTANCE = new DefaultModule();

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Class<?>, Entry>> entry() {
        return Optional.of(new Interceptor<Class<?>, Entry>() {
            @Override
            public boolean before(Context context, Class<?> source) {
                return !StringUtils.startsWithAny(source.getTypeName(), "java.util", "java.lang", "javax.servlet");
            }
        });
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Field, EntryField>> entryField() {
        return Optional.of(new Interceptor<Field, EntryField>() {
            @Override
            public boolean before(Context context, Field source) {
                if (StringUtils.equalsAny(source.getName(), "serialVersionUID", "$VALUES")) {
                    return false;
                }
                Class<?> declaringClass = source.getDeclaringClass();
                if (Enum.class.isAssignableFrom(declaringClass)) {
                    if (StringUtils.equalsAny(source.getName(), "name", "ordinal")) {
                        return false;
                    }
                }
                return true;

            }
        });
    }

    /**
     * 方法拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Method, EntryMethod>> entryMethod() {
        return Optional.of(new Interceptor<Method, EntryMethod>() {
            @Override
            public boolean before(Context context, Method source) {
                return StringUtils.startsWithAny(source.getName(), "get", "is");
            }
        });
    }

    /**
     * 请求参数拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<RequestParam, RequestParam>> requestParam() {
        return Optional.of(new Interceptor<RequestParam, RequestParam>() {

            @Override
            public boolean before(Context context, RequestParam source) {
                String type = source.getType().getType();
                return !StringUtils.startsWith(type, "javax.servlet");
            }
        });
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }
}
