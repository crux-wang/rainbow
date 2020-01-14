package ren.crux.rainbow.core.report.mock;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wangzhihui
 */
public class DateTimeMockers {

    public static final DateMocker DATE = new DateMocker();
    public static final SqlDateMocker SQL_DATE = new SqlDateMocker();
    public static final TimestampMocker TIMESTAMP = new TimestampMocker();
    public static final TimeMocker TIME = new TimeMocker();
    public static final Map<Class, DataMocker> DATE_TIME = new HashMap<>();
    private static final long ONE_YEARS = 365 * 24 * 60 * 60 * 1000L;

    static {
        DATE_TIME.put(Date.class, DATE);
        DATE_TIME.put(java.sql.Date.class, SQL_DATE);
        DATE_TIME.put(Timestamp.class, TIMESTAMP);
        DATE_TIME.put(Time.class, TIME);
    }

    public static class DateMocker implements DataMocker<Date> {
        @Override
        public Optional<Date> mock(DataMockerContext context) {
            Past past = context.getAnnotation(Past.class);
            PastOrPresent pastOrPresent = context.getAnnotation(PastOrPresent.class);
            Future future = context.getAnnotation(Future.class);
            FutureOrPresent futureOrPresent = context.getAnnotation(FutureOrPresent.class);
            long origin = 0;
            long bound = System.currentTimeMillis() + ONE_YEARS;
            long now = System.currentTimeMillis();
            if (past != null) {
                bound = now - 1;
            } else if (pastOrPresent != null) {
                bound = now;
            } else if (future != null) {
                origin = now + 1;
            } else if (futureOrPresent != null) {
                origin = now;
            }
            return Optional.of(new Date(ThreadLocalRandom.current().nextLong(origin, bound)));
        }
    }

    public static class SqlDateMocker implements DataMocker<java.sql.Date> {
        @Override
        public Optional<java.sql.Date> mock(DataMockerContext context) {
            try {
                Optional<Date> optional = context.getMockers().mock(context.createSubMockerContext(Date.class), null);
                if (optional.isPresent()) {
                    return Optional.of(new java.sql.Date(optional.get().getTime()));
                }
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }
    }

    public static class TimestampMocker implements DataMocker<Timestamp> {
        @Override
        public Optional<Timestamp> mock(DataMockerContext context) {
            try {
                Optional<Date> optional = context.getMockers().mock(context.createSubMockerContext(Date.class), null);
                if (optional.isPresent()) {
                    return Optional.of(new Timestamp(optional.get().getTime()));
                }
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }
    }

    public static class TimeMocker implements DataMocker<Time> {
        @Override
        public Optional<Time> mock(DataMockerContext context) {
            try {
                Optional<Date> optional = context.getMockers().mock(context.createSubMockerContext(Date.class), null);
                if (optional.isPresent()) {
                    return Optional.of(new Time(optional.get().getTime()));
                }
            } catch (Exception ignored) {
            }
            return Optional.empty();
        }
    }
}
