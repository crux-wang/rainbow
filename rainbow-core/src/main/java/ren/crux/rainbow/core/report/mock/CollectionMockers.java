package ren.crux.rainbow.core.report.mock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wangzhihui
 */
@Slf4j
public class CollectionMockers {

    public static final SetMocker SET = new SetMocker();
    public static final MapMocker MAP = new MapMocker();
    public static final ListMocker LIST = new ListMocker();

    public static final Map<Class, DataMocker> COLLECTION = new HashMap<>();

    static {
        COLLECTION.put(Collection.class, LIST);
        COLLECTION.put(Set.class, SET);
        COLLECTION.put(Map.class, MAP);
        COLLECTION.put(List.class, LIST);
    }

    public static class SetMocker implements DataMocker<Set> {

        @Override
        public Optional<Set> mock(DataMockerContext context) {
            try {
                Optional<List> optional = context.getMockers().mock(context.createTryMockerContext(List.class), null);
                if (optional.isPresent()) {
                    return Optional.of(new HashSet(optional.get()));
                }
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }

    }

    public static class ListMocker implements DataMocker<List> {

        @Override
        public Optional<List> mock(DataMockerContext context) {
            Type actualTypeArgument = context.getActualTypeArgument(0)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid actual type argument."));
            Objects.requireNonNull(actualTypeArgument);
            Object array = Array.newInstance((Class<?>) actualTypeArgument, 0);
            try {
                array = context.getMockers().mock(context.createSubMockerContext(array.getClass()), array).orElse(null);
            } catch (Exception ignored) {
            }
            if (array != null) {
                return Optional.of(Arrays.stream((Object[]) array).collect(Collectors.toList()));
            }
            return Optional.empty();
        }
    }

    public static class MapMocker implements DataMocker<Map> {

        @Override
        public Optional<Map> mock(DataMockerContext context) {
            int len = ValidationHelper.getLength(context);
            Type keyType = context.getActualTypeArgument(0)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid actual type argument."));
            Type valType = context.getActualTypeArgument(1)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid actual type argument."));
            Objects.requireNonNull(keyType);
            Objects.requireNonNull(valType);
            List<Pair> pairs = new ArrayList<>(len);
            for (int i = 0; i < len; i++) {
                try {
                    Optional<Object> optional = context.getMockers().mock(context.createSubMockerContext(keyType), null);
                    if (optional.isPresent()) {
                        Object key = optional.get();
                        optional = context.getMockers().mock(context.createSubMockerContext(valType), null);
                        optional.ifPresent(o -> pairs.add(Pair.of(key, o)));
                    }

                } catch (Exception ignored) {
                }
            }
            if (len > 0 && pairs.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(pairs.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue)));
        }
    }

}
