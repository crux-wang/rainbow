package ren.crux.rainbow.core.report.mock;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangzhihui
 */
@Slf4j
public class Mockers {

    private Map<Class, DataMocker> mockers = new ConcurrentHashMap<>();

    public Mockers() {
        mockers.putAll(BasicMockers.BASIC);
        mockers.putAll(CollectionMockers.COLLECTION);
        mockers.putAll(DateTimeMockers.DATE_TIME);
    }

    public <T> void register(@NonNull Class<T> type, @NonNull DataMocker<T> mocker) {
        mockers.put(type, mocker);
    }

    public <T> DataMocker unregister(@NonNull Class<T> type) {
        return mockers.remove(type);
    }

    public DataMocker getMocker(Class type) {
        return mockers.get(type);
    }

    public <T> Optional<T> mockBasic(Class<T> type) {
        return tryMock(new DataMockerContext(type, this));
    }

    public <T> Optional<T> mock(@NonNull Class<T> type) {
        return mockOfInstance(type, null);
    }

    public <T> Optional<T> mock(@NonNull Class<T> type, Type[] actualTypeArguments) {
        return mock(new DataMockerContext(type, actualTypeArguments, null, this), null);
    }

    public <T> Optional<T> mockOfInstance(@NonNull Class<T> type, T instance) {
        return mock(new DataMockerContext(type, this), instance);
    }

    public <T> Optional<T> mockOfInstance(@NonNull Class<T> type, Type[] actualTypeArguments, T instance) {
        return mock(new DataMockerContext(type, actualTypeArguments, null, this), instance);
    }

    protected <T> Optional<T> mock(DataMockerContext context, T instance) {
        Class type = context.getType();
        Optional<T> optional = tryMock(context);
        if (optional.isPresent()) {
            return optional;
        }
        if (instance == null) {
            try {
                instance = (T) type.newInstance();
            } catch (Exception e) {
                return Optional.empty();
            }
        }
        if (context.contains(type)) {
            log.warn("ignored mock cycle class : {}", type);
            return Optional.ofNullable(instance);
        }
        Set<Field> fields = new HashSet<>();
        Class t = type;
        while (t != Object.class) {
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
            fields.addAll(Arrays.asList(type.getFields()));
            t = t.getSuperclass();
        }
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, instance);
            Optional<Object> opt = mock(context.createSubMockerContext(field), value);
            if (opt.isPresent()) {
                ReflectionUtils.setField(field, instance, opt.get());
            }
        }
        return Optional.ofNullable(instance);
    }

    protected <T> Optional<T> tryMock(@NonNull DataMockerContext context) {
        try {
            DataMocker<T> mocker = mockers.get(context.getType());
            if (mocker == null) {
                if (context.getType().isEnum()) {
                    mocker = mockers.get(Enum.class);
                } else if (context.getType().isArray()) {
                    mocker = mockers.get(Array.class);
                }
            }
            if (mocker != null) {
                return mocker.mock(context);
            }
        } catch (RuntimeException ignored) {
        }
        return Optional.empty();
    }

}
