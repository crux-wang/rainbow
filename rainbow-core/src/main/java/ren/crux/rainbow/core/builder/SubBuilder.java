package ren.crux.rainbow.core.builder;

public abstract class SubBuilder<S> {

    protected final S superBuilder;

    public SubBuilder(S superBuilder) {
        this.superBuilder = superBuilder;
    }

    public S end() {
        return superBuilder;
    }

}
