package ren.crux.rainbow.core.module;

import com.sun.javadoc.Doc;
import ren.crux.rainbow.core.interceptor.Interceptor;
import ren.crux.rainbow.core.option.Option;
import ren.crux.raonbow.common.model.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 解析器选项模块
 *
 * @author wangzhihui
 */
public class ParserOptionModule implements Module {

    public static final ParserOptionModule INSTANCE = new ParserOptionModule();


    /**
     * 在 {@link Context#setOption(Option, Object)} Option
     */
    public static final Option<IgnoredOption[]> IGNORED_OPTION = Option.valueOf("IGNORED_OPTION", IgnoredOption[].class);
    /**
     * 忽略选项
     */
    private Set<IgnoredOption> ignoredOptions;

    /**
     * 类拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Class<?>, Entry>> entry() {
        return Optional.empty();
    }

    /**
     * 属性拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Field, EntryField>> entryField() {
        return Optional.empty();
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
                return !ignoredOptions.contains(IgnoredOption.entry_method);
            }
        });
    }

    /**
     * 注解拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Annotation, ren.crux.raonbow.common.model.Annotation>> annotation() {
        return Optional.of(new Interceptor<Annotation, ren.crux.raonbow.common.model.Annotation>() {
            @Override
            public boolean before(Context context, Annotation source) {
                return !ignoredOptions.contains(IgnoredOption.annotation);
            }

            @Override
            public boolean after(Context context, ren.crux.raonbow.common.model.Annotation target) {
                if (ignoredOptions.contains(IgnoredOption.annotation_attrs)) {
                    target.setAttribute(null);
                }
                return true;
            }
        });
    }

    /**
     * 注释拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Doc, CommentText>> commentText() {
        return Optional.of(new Interceptor<Doc, CommentText>() {
            @Override
            public boolean after(Context context, CommentText target) {
                if (ignoredOptions.contains(IgnoredOption.tags)) {
                    target.setTags(null);
                }
                return true;
            }
        });
    }

    /**
     * 请求拦截器
     *
     * @return 拦截器构造器
     */
    @Override
    public Optional<Interceptor<Request, Request>> request() {
        return Optional.of(new Interceptor<Request, Request>() {
            @Override
            public boolean after(Context context, Request target) {
                if (ignoredOptions.contains(IgnoredOption.signature)) {
                    target.setSignature(null);
                    target.setDeclaringType(null);
                }
                return true;
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
            public boolean after(Context context, RequestParam target) {
                if (ignoredOptions.contains(IgnoredOption.signature)) {
                    target.setDeclaringSignature(null);
                }
                return true;
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
        return Optional.empty();
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
     * 加载前初始化
     *
     * @param context 上下文
     */
    @Override
    public void setUp(Context context) {
        if (this.ignoredOptions == null) {
            IgnoredOption[] option = context.getOption(IGNORED_OPTION);
            if (option == null) {
                return;
            }
            this.ignoredOptions = Arrays.stream(option).collect(Collectors.toSet());
        } else {
            context.setOption(IGNORED_OPTION, this.ignoredOptions.toArray(new IgnoredOption[0]));
        }
    }

    /**
     * 加载序号
     *
     * @return 序号
     */
    @Override
    public int order() {
        return 1;
    }

    /**
     * 忽略选项
     */
    public static enum IgnoredOption {
        /**
         * 注解属性
         */
        annotation_attrs,
        /**
         * 注解
         */
        annotation,
        /**
         * 标签
         */
        tags,
        /**
         * 签名
         */
        signature,
        /**
         * 实体方法
         */
        entry_method,
    }
}
