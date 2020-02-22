package ren.crux.rainbow.core.report.mock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import javax.validation.constraints.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wangzhihui
 */
@Slf4j
public class BasicMockers {

    public static final ObjectMocker OBJECT = new ObjectMocker();
    public static final IntegerMocker INTEGER = new IntegerMocker();
    public static final LongMocker LONG = new LongMocker();
    public static final DoubleMocker DOUBLE = new DoubleMocker();
    public static final ShortMocker SHORT = new ShortMocker();
    public static final CharMocker CHAR = new CharMocker();
    public static final BooleanMocker BOOLEAN = new BooleanMocker();
    public static final FloatMocker FLOAT = new FloatMocker();
    public static final ByteMocker BYTE = new ByteMocker();
    public static final StringMocker STRING = new StringMocker();
    public static final BytesMocker BYTES = new BytesMocker();
    public static final ArraysMocker ARRAYS = new ArraysMocker();
    public static final Map<Class, DataMocker> BASIC = new HashMap<>();
    private static final EnumMocker ENUM = new EnumMocker();

    static {
        BASIC.put(Object.class, OBJECT);
        BASIC.put(Integer.class, INTEGER);
        BASIC.put(int.class, INTEGER);
        BASIC.put(Long.class, LONG);
        BASIC.put(long.class, LONG);
        BASIC.put(Double.class, DOUBLE);
        BASIC.put(double.class, LONG);
        BASIC.put(Short.class, SHORT);
        BASIC.put(short.class, SHORT);
        BASIC.put(Character.class, CHAR);
        BASIC.put(char.class, CHAR);
        BASIC.put(Boolean.class, BOOLEAN);
        BASIC.put(boolean.class, BOOLEAN);
        BASIC.put(Float.class, FLOAT);
        BASIC.put(float.class, FLOAT);
        BASIC.put(Byte.class, BYTE);
        BASIC.put(byte.class, BYTE);
        BASIC.put(String.class, STRING);
        BASIC.put(Enum.class, ENUM);
        BASIC.put(byte[].class, BYTES);
        BASIC.put(Array.class, ARRAYS);
    }

    public static class IntegerMocker implements DataMocker<Integer> {
        @Override
        public Optional<Integer> mock(DataMockerContext context) {
            Min min = context.getAnnotation(Min.class);
            Max max = context.getAnnotation(Max.class);
            Positive positive = context.getAnnotation(Positive.class);
            PositiveOrZero positiveOrZero = context.getAnnotation(PositiveOrZero.class);
            NegativeOrZero negativeOrZero = context.getAnnotation(NegativeOrZero.class);
            Negative negative = context.getAnnotation(Negative.class);
            int origin = Integer.MIN_VALUE;
            int bound = Integer.MAX_VALUE;
            if (min != null) {
                origin = Math.toIntExact(min.value());
            } else if (positive != null) {
                origin = 1;
            } else if (positiveOrZero != null) {
                origin = 0;
            }
            if (max != null) {
                bound = Math.toIntExact(max.value());
            } else if (negative != null) {
                bound = -1;
            } else if (negativeOrZero != null) {
                bound = 0;
            }
            return Optional.of(ThreadLocalRandom.current().nextInt(origin, bound));
        }
    }

    public static class LongMocker implements DataMocker<Long> {
        @Override
        public Optional<Long> mock(DataMockerContext context) {
            Min min = context.getAnnotation(Min.class);
            Max max = context.getAnnotation(Max.class);
            Positive positive = context.getAnnotation(Positive.class);
            PositiveOrZero positiveOrZero = context.getAnnotation(PositiveOrZero.class);
            NegativeOrZero negativeOrZero = context.getAnnotation(NegativeOrZero.class);
            Negative negative = context.getAnnotation(Negative.class);
            long origin = Long.MIN_VALUE;
            long bound = Long.MAX_VALUE;
            if (min != null) {
                origin = min.value();
            } else if (positive != null) {
                origin = 1;
            } else if (positiveOrZero != null) {
                origin = 0;
            }
            if (max != null) {
                bound = max.value();
            } else if (negative != null) {
                bound = -1;
            } else if (negativeOrZero != null) {
                bound = 0;
            }
            return Optional.of(ThreadLocalRandom.current().nextLong(origin, bound));
        }
    }

    public static class ShortMocker implements DataMocker<Short> {

        @Override
        public Optional<Short> mock(DataMockerContext context) {
            Min min = context.getAnnotation(Min.class);
            Max max = context.getAnnotation(Max.class);
            Positive positive = context.getAnnotation(Positive.class);
            PositiveOrZero positiveOrZero = context.getAnnotation(PositiveOrZero.class);
            NegativeOrZero negativeOrZero = context.getAnnotation(NegativeOrZero.class);
            Negative negative = context.getAnnotation(Negative.class);
            short origin = Short.MIN_VALUE;
            short bound = Short.MAX_VALUE;
            if (min != null) {
                origin = (short) min.value();
            } else if (positive != null) {
                origin = 1;
            } else if (positiveOrZero != null) {
                origin = 0;
            }
            if (max != null) {
                bound = (short) max.value();
            } else if (negative != null) {
                bound = -1;
            } else if (negativeOrZero != null) {
                bound = 0;
            }
            return Optional.of((short) (ThreadLocalRandom.current().nextInt(origin, bound)));
        }

    }

    public static class DoubleMocker implements DataMocker<Double> {
        @Override
        public Optional<Double> mock(DataMockerContext context) {
            Positive positive = context.getAnnotation(Positive.class);
            PositiveOrZero positiveOrZero = context.getAnnotation(PositiveOrZero.class);
            NegativeOrZero negativeOrZero = context.getAnnotation(NegativeOrZero.class);
            Negative negative = context.getAnnotation(Negative.class);
            double origin = Double.MIN_VALUE;
            double bound = Double.MAX_VALUE;
            if (positive != null) {
                origin = 1;
            } else if (positiveOrZero != null) {
                origin = 0;
            } else if (negative != null) {
                bound = -1;
            } else if (negativeOrZero != null) {
                bound = 0;
            }
            return Optional.of(ThreadLocalRandom.current().nextDouble(origin, bound));
        }
    }

    public static class FloatMocker implements DataMocker<Float> {
        @Override
        public Optional<Float> mock(DataMockerContext context) {
            Positive positive = context.getAnnotation(Positive.class);
            PositiveOrZero positiveOrZero = context.getAnnotation(PositiveOrZero.class);
            NegativeOrZero negativeOrZero = context.getAnnotation(NegativeOrZero.class);
            Negative negative = context.getAnnotation(Negative.class);
            if (positive != null || positiveOrZero != null) {
                return Optional.of(Math.abs(ThreadLocalRandom.current().nextFloat()));
            }
            if (negative != null || negativeOrZero != null) {
                return Optional.of(-1 * Math.abs(ThreadLocalRandom.current().nextFloat()));
            }
            return Optional.of(ThreadLocalRandom.current().nextFloat());
        }
    }

    public static class ByteMocker implements DataMocker<Byte> {
        @Override
        public Optional<Byte> mock(DataMockerContext context) {
            Positive positive = context.getAnnotation(Positive.class);
            PositiveOrZero positiveOrZero = context.getAnnotation(PositiveOrZero.class);
            NegativeOrZero negativeOrZero = context.getAnnotation(NegativeOrZero.class);
            Negative negative = context.getAnnotation(Negative.class);
            byte[] bytes = new byte[1];
            ThreadLocalRandom.current().nextBytes(bytes);
            byte b = bytes[0];
            if (positive != null || positiveOrZero != null) {
                return Optional.of((byte) Math.abs(b));
            }
            if (negative != null || negativeOrZero != null) {
                return Optional.of((byte) (-1 * Math.abs(b)));
            }
            return Optional.of(b);
        }
    }

    public static class BytesMocker implements DataMocker<byte[]> {
        @Override
        public Optional<byte[]> mock(DataMockerContext context) {
            Size size = context.getAnnotation(Size.class);
            int len = size != null ? ThreadLocalRandom.current().nextInt(size.min(), size.max()) : 16;
            byte[] bytes = new byte[len];
            ThreadLocalRandom.current().nextBytes(bytes);
            return Optional.of(bytes);
        }
    }

    public static class CharMocker implements DataMocker<Character> {
        @Override
        public Optional<Character> mock(DataMockerContext context) {
            return Optional.of(RandomStringUtils.random(1, true, true).charAt(0));
        }
    }

    public static class StringMocker implements DataMocker<String> {
        @Override
        public Optional<String> mock(DataMockerContext context) {
            Size size = context.getAnnotation(Size.class);
            Email email = context.getAnnotation(Email.class);
            Annotation url = context.getAnnotation("org.hibernate.validator.constraints.URL");
            NotBlank notBlank = context.getAnnotation(NotBlank.class);
            int min = 2;
            int max = 16;
            if (size != null) {
                min = size.min();
                max = size.max();
            }
            if (email != null) {
                if (max < 5) {
                    throw new IllegalArgumentException("email length too small");
                }
                if (min < 5) {
                    min = 5;
                }
            }
            if (max > 128) {
                max = 128;
            }
            if (notBlank != null && min <= 0) {
                min = 1;
            }
            int len = min == max ? min : ThreadLocalRandom.current().nextInt(min, max);
            if (email != null) {
                // 不包括 @ 和 .
                max -= 2;
                int userNameLen = ThreadLocalRandom.current().nextInt(1, max - 2);
                int domainLen = (max - userNameLen) / 2;
                int tldLen = max - userNameLen - domainLen;
                String userName = RandomStringUtils.random(userNameLen, true, true);
                String domain = RandomStringUtils.random(domainLen, true, true);
                String tld = RandomStringUtils.random(tldLen, true, false);
                return Optional.of(userName + "@" + domain + "." + tld);
            } else if (url != null) {
                return Optional.of("http://staging-cnbj2-fds.api.xiaomi.net/haoda-material/c50881fb-9286-4bc5-a896-53b500a93987.png");
            }
            return Optional.of(RandomStringUtils.random(len, true, true));
        }
    }

    public static class BooleanMocker implements DataMocker<Boolean> {
        @Override
        public Optional<Boolean> mock(DataMockerContext context) {
            if (context.getAnnotation(AssertTrue.class) != null) {
                return Optional.of(true);
            }
            if (context.getAnnotation(AssertFalse.class) != null) {
                return Optional.of(false);
            }
            return Optional.of(ThreadLocalRandom.current().nextBoolean());
        }
    }

    public static class EnumMocker implements DataMocker<Object> {

        @Override
        public Optional<Object> mock(DataMockerContext context) {
            if (context.isEnum()) {
                Object[] values = context.getType().getEnumConstants();
                int idx = ThreadLocalRandom.current().nextInt(0, values.length);
                return Optional.of(values[idx]);
            }
            return Optional.empty();
        }

    }

    public static class ArraysMocker implements DataMocker<Object> {

        @Override
        public Optional<Object> mock(DataMockerContext context) {
            if (context.isArray()) {
                int len = ValidationHelper.getLength(context);
                Type actualTypeArgument = context.getActualTypeArgument(0)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid actual type argument."));
                Object arr = Array.newInstance((Class<?>) actualTypeArgument, len);
                for (int i = 0; i < len; i++) {
                    try {
                        Object val = context.getMockers().mock(context.createSubMockerContext(actualTypeArgument), null)
                                .orElseThrow(Exception::new);
                        Array.set(arr, i, val);
                    } catch (Exception ignored) {
                        return Optional.empty();
                    }
                }
                return Optional.of(arr);
            }
            return Optional.empty();
        }

    }

    public static class ObjectMocker implements DataMocker<Object> {

        @Override
        public Optional<Object> mock(DataMockerContext context) {
            return Optional.of(Collections.singletonMap("key", "any"));
        }

    }
}
