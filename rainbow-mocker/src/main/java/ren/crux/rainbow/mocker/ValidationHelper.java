package ren.crux.rainbow.mocker;

import javax.validation.constraints.Size;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wangzhihui
 */
public class ValidationHelper {

    public static int getLength(DataMockerContext context) {
        Size size = context.getAnnotation(Size.class);
        int min = size == null ? 2 : size.min();
        int max = size == null ? 4 : size.max();
        if (max > 16) {
            max = 16;
        }
        if (min == max) {
            return max;
        }
        if (min > max) {
            max = min + 4;
        }
        return ThreadLocalRandom.current().nextInt(min, max);
    }

}
