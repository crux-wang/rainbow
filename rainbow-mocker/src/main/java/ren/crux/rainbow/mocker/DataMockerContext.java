package ren.crux.rainbow.mocker;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
@Slf4j
@Data
public class DataMockerContext {

    private final List<Type> actualTypeArguments;
    private final Map<Class, Annotation> annotations;
    private final Mockers mockers;
    private final List<Class> superClasses = new ArrayList<>();
    private Class type;

    public DataMockerContext(@NonNull Class type, @NonNull Mockers mockers) {
        this(type, getActualTypeArguments(type),
                type.getDeclaredAnnotations(), mockers);
    }

    public DataMockerContext(@NonNull Field field, @NonNull Mockers mockers) {
        this(field.getType(), getActualTypeArguments(field),
                field.getDeclaredAnnotations(), mockers);
    }

    public DataMockerContext(@NonNull Class type, Type[] actualTypeArguments, Annotation[] annotations, @NonNull Mockers mockers) {
        this.type = type;
        this.mockers = mockers;
        if (actualTypeArguments == null) {
            this.actualTypeArguments = Collections.emptyList();
        } else {
            this.actualTypeArguments = Collections.unmodifiableList(Arrays.asList(actualTypeArguments));
        }
        if (annotations == null) {
            this.annotations = Collections.emptyMap();
        } else {
            this.annotations = Collections.unmodifiableMap(Arrays.stream(annotations)
                    .collect(Collectors.toMap(Annotation::annotationType, a -> a)));
        }
    }

    public static Type[] getActualTypeArguments(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            return pt.getActualTypeArguments();
        }
        return null;
    }

    public static Type[] getActualTypeArguments(Class type) {
        if (type.isArray()) {
            return new Type[]{type.getComponentType()};
        }
        if (type.getGenericSuperclass() instanceof ParameterizedType) {
            return ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        }
        return null;
    }

    public static Type[] getActualTypeArguments(Type type) {

        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments();
        }
        return null;
    }

    public boolean contains(Class type) {
        return superClasses.contains(type);
    }

    public void addSuperClass(Class type) {
        superClasses.add(type);
    }

    public DataMockerContext createSubMockerContext(Class type) {
        DataMockerContext subMockerContext = new DataMockerContext(type, this.mockers);
        subMockerContext.superClasses.addAll(this.superClasses);
        subMockerContext.addSuperClass(this.type);
        return subMockerContext;
    }

    public DataMockerContext createTryMockerContext(Class type) {
        DataMockerContext subMockerContext = new DataMockerContext(type, this.mockers);
        subMockerContext.superClasses.addAll(this.superClasses);
        return subMockerContext;
    }

    public DataMockerContext createSubMockerContext(Type type) {
        Type[] actualTypeArguments = null;
        if (type instanceof ParameterizedType) {
            actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            type = ((ParameterizedType) type).getRawType();
        }
        DataMockerContext subMockerContext = new DataMockerContext((Class) type, actualTypeArguments, null, this.mockers);
        subMockerContext.superClasses.addAll(this.superClasses);
        return subMockerContext;
    }

    public DataMockerContext createSubMockerContext(Field field) {
        DataMockerContext subMockerContext = new DataMockerContext(field, this.mockers);
        subMockerContext.superClasses.addAll(this.superClasses);
        subMockerContext.addSuperClass(this.type);
        return subMockerContext;
    }

    public DataMockerContext createTryMockerContext(Field field) {
        DataMockerContext subMockerContext = new DataMockerContext(field, this.mockers);
        subMockerContext.superClasses.addAll(this.superClasses);
        return subMockerContext;
    }

    @SuppressWarnings("unchecked")
    public <E extends Annotation> E getAnnotation(Class<E> annotation) {
        return (E) annotations.get(annotation);
    }

    @SuppressWarnings("unchecked")
    public <E extends Annotation> E getAnnotation(String annotationClassName) {
        return (E) annotations.values().stream().filter(annotation -> annotation.annotationType().getCanonicalName().equals(annotationClassName)).findFirst().orElse(null);
    }


    public Optional<Type> getActualTypeArgument(int idx) {
        if (idx < actualTypeArguments.size()) {
            return Optional.of(actualTypeArguments.get(idx));
        }
        return Optional.empty();
    }

    public Optional<DataMocker> getMocker(Class type) {
        return Optional.ofNullable(mockers.getMocker(type));
    }

    public boolean isEnum() {
        return type.isEnum();
    }

    public boolean isArray() {
        return type.isArray();
    }

    public boolean isInterface() {
        return type.isInterface();
    }
}
