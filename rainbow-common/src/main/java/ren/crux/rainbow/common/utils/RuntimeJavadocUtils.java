/*
 *  Copyright 2020. The Crux Authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ren.crux.rainbow.common.utils;

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

    public static String getComment(Field field) {
        return format(RuntimeJavadoc.getJavadoc(field).getComment());
    }

    public static String getComment(Method method) {
        return format(RuntimeJavadoc.getJavadoc(method).getComment());
    }

    public static String getComment(Class<?> clazz) {
        return format(RuntimeJavadoc.getJavadoc(clazz).getComment());
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
                    .filter(tag -> tag != null && StringUtils.equals(tag.getName(), paramName))
                    .findFirst().map(RuntimeJavadocUtils::formatComment);
        }
        return Optional.empty();
    }

    public static Optional<String> getParamComment(Method method, int index) {
        MethodJavadoc methodJavadoc = RuntimeJavadoc.getJavadoc(method);
        List<ParamJavadoc> params = methodJavadoc.getParams();
        if (params != null) {
            if (params.size() > index) {
                return Optional.of(format(params.get(index).getComment()));
            }
        }
        return Optional.empty();
    }

    private static String formatComment(ParamJavadoc paramJavadoc) {
        return paramJavadoc == null ? "" : format(paramJavadoc.getComment());
    }

    public static String format(Comment comment) {
        return COMMENT_FORMATTER.format(comment);
    }

    public static String summary(Comment comment) {
        if (comment == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (CommentElement e : comment) {
            if (e instanceof CommentText) {
                sb.append(((CommentText) e).getValue());
            }
        }
        return sb.toString();
    }

}
