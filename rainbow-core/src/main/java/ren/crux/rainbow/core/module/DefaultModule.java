package ren.crux.rainbow.core.module;

public class DefaultModule extends AbstractModule {

    private final String name;

    public DefaultModule(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
