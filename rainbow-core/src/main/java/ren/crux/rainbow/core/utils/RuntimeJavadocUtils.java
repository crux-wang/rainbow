package ren.crux.rainbow.core.utils;

import com.github.therapi.runtimejavadoc.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * RuntimeJavadocUtils
 *
 * @author wangzhihui
 */
public class RuntimeJavadocUtils {

    private final static CommentFormatter COMMENT_FORMATTER = new CommentFormatter();

    public static List<MethodJavadoc> getAllMethodJavadoc(Class<?> clazz) {
        List<MethodJavadoc> methods = new LinkedList<>();
        while (clazz != Object.class) {
            ClassJavadoc classJavadoc = getJavadoc(clazz);
            methods.addAll(classJavadoc.getMethods());
            clazz = clazz.getSuperclass();
        }
        return methods;
    }

    public static FieldJavadoc getJavadoc(Field field) {
        return RuntimeJavadoc.getJavadoc(field);
    }

    public static MethodJavadoc getJavadoc(Method method) {
        return RuntimeJavadoc.getJavadoc(method);
    }

    public static ClassJavadoc getJavadoc(Class<?> clazz) {
        return RuntimeJavadoc.getJavadoc(clazz);
    }

    public static Optional<OtherJavadoc> getTag(Class<?> clazz, String tagName) {
        ClassJavadoc classJavadoc = RuntimeJavadoc.getJavadoc(clazz);
        if (classJavadoc.getOther() != null) {
            return classJavadoc.getOther().stream()
                    .filter(tag -> StringUtils.equals(StringUtils.trim(tag.getName()), tagName))
                    .findFirst();
        }
        return Optional.empty();
    }


    public static Optional<String> getParamComment(Method method, String paramName) {
        MethodJavadoc methodJavadoc = RuntimeJavadoc.getJavadoc(method);
        if (methodJavadoc.getParams() != null) {
            return methodJavadoc.getParams().stream()
                    .filter(tag -> StringUtils.equals(tag.getName(), paramName))
                    .findFirst().map(RuntimeJavadocUtils::formatComment);
        }
        return Optional.empty();
    }

    private static String formatComment(ParamJavadoc paramJavadoc) {
        return paramJavadoc == null ? "" : format(paramJavadoc.getComment());
    }

    public static String format(Comment comment) {
        return COMMENT_FORMATTER.format(comment);
    }
}
