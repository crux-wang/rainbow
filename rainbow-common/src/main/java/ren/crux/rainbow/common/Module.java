package ren.crux.rainbow.common;

/**
 * 模块
 *
 * @param <C> 上下文
 * @author wangzhihui
 */
public interface Module<C> {
    /**
     * 配置类拦截器
     *
     * @return 拦截器构造器
     */
    default <S, T> CombinationInterceptor.CombinationInterceptorBuilder<S, T, C> clazz() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置属性拦截器
     *
     * @return 拦截器构造器
     */
    default <S, T> CombinationInterceptor.CombinationInterceptorBuilder<S, T, C> field() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置方法拦截器
     *
     * @return 拦截器构造器
     */
    default <S, T> CombinationInterceptor.CombinationInterceptorBuilder<S, T, C> method() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置参数拦截器
     *
     * @return 拦截器构造器
     */
    default <S, T> CombinationInterceptor.CombinationInterceptorBuilder<S, T, C> parameter() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置注解拦截器
     *
     * @return 拦截器构造器
     */
    default <S, T> CombinationInterceptor.CombinationInterceptorBuilder<S, T, C> annotation() {
        return CombinationInterceptor.builder();
    }

    /**
     * 配置注释拦截器
     *
     * @return 拦截器构造器
     */
    default <S, T> CombinationInterceptor.CombinationInterceptorBuilder<S, T, C> commentText() {
        return CombinationInterceptor.builder();
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

}
