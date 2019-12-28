package ren.crux.rainbow.runtime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.http.HttpEntity;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.module.Context;
import ren.crux.rainbow.core.module.Module;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * SpringWebModule
 *
 * @author wangzhihui
 **/
public class SpringWebModule implements Module {

    public static final SpringWebModule INSTANCE = new SpringWebModule();

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
                if (StringUtils.startsWith(source.getTypeName(), "org.springframework.web.servlet")) {
                    return false;
                }
                if (HttpEntity.class.isAssignableFrom(source)) {
                    return false;
                }
                return true;
            }
        });
    }


    /**
     * 注解拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Annotation, ren.crux.rainbow.core.model.Annotation>> annotation() {
        return Optional.empty();
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
                return !StringUtils.startsWith(type, "org.springframework.web.servlet");
            }
        });
    }

    /**
     * 请求组拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<RequestGroup, RequestGroup>> requestGroup() {
        return Optional.of(new Interceptor<RequestGroup, RequestGroup>() {
            @Override
            public boolean before(Context context, RequestGroup source) {
                return !StringUtils.equals(BasicErrorController.class.getTypeName(), source.getType());
            }
        });
    }

    /**
     * 模块名
     *
     * @return 模块名
     */
    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    /**
     * 加载序号
     *
     * @return 序号
     */
    @Override
    public int order() {
        return 2;
    }

    /**
     * 加载前初始化
     *
     * @param context 上下文
     */
    @Override
    public void setUp(Context context) {

    }

    /**
     * 接口实现映射
     *
     * @return 接口实现映射
     */
    @Override
    public Map<String, String> implMap() {
        Map<String, String> map = new HashMap<>(2);
        map.put("org.springframework.data.domain.Page", "org.springframework.data.domain.PageImpl");
        map.put("org.springframework.data.domain.Pageable", "org.springframework.data.domain.PageRequest");
        return map;
    }
}
