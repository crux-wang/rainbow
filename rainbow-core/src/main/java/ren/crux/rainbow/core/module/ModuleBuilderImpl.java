
package ren.crux.rainbow.core.module;

public class ModuleBuilderImpl implements ModuleBuilder {

    private String name;
    private int order;

    /**
     * 设置模块名
     *
     * @param name 模块名
     * @return 自身
     */
    @Override
    public ModuleBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * 设置加载序号
     *
     * @param order 序号
     * @return 自身
     */
    @Override
    public ModuleBuilder order(int order) {
        this.order = order;
        return this;
    }

    /**
     * 构建
     *
     * @return 模块
     */
    @Override
    public Module build() {
        return new ModuleImpl(name, order, clazz().build(), field().build(), method().build(), parameter().build(), annotation().build(), commentText().build());
    }
}
