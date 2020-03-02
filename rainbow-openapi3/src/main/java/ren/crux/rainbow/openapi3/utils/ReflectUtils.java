package ren.crux.rainbow.openapi3.utils;

import lombok.SneakyThrows;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public class ReflectUtils {

    @SneakyThrows
    public static int getParameterIndex(Parameter parameter) {
        Field indexField = Parameter.class.getDeclaredField("index");
        ReflectionUtils.makeAccessible(indexField);
        return (int) ReflectionUtils.getField(indexField, parameter);
    }

}
