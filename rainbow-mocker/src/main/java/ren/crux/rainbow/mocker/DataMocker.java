package ren.crux.rainbow.mocker;

import java.util.Optional;

/**
 * @author wangzhihui
 */
public interface DataMocker<T> {

    /**
     * 模拟数据
     *
     * @param context 上下文
     * @return
     */
    Optional<T> mock(DataMockerContext context);

}
