package ren.crux.rainbow.core.tuple;

/**
 * 可描述的
 *
 * @author wangzhihui
 */
public interface Describable {

    /**
     * 获取名称
     *
     * @return 名称
     */
    default String getName() {
        return null;
    }

    /**
     * 获取描述
     *
     * @return 描述
     */
    default String getDescription() {
        return null;
    }

}
