package ren.crux.rainbow.core.module;

import ren.crux.rainbow.core.Context;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.EntryField;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.module.enhancer.EntryEnhancer;
import ren.crux.rainbow.core.module.enhancer.EntryFieldEnhancer;
import ren.crux.rainbow.core.module.enhancer.RequestEnhancer;
import ren.crux.rainbow.core.module.enhancer.RequestGroupEnhancer;
import ren.crux.rainbow.core.module.filter.*;

public abstract class AbstractModule implements Module {

    protected RequestGroupFilter requestGroupFilter;
    protected RequestFilter requestFilter;
    protected EntryFilter entryFilter;
    protected EntryFieldFilter entryFieldFilter;
    protected EntryClassNameFilter entryClassNameFilter;
    protected EntryClassFilter entryClassFilter;

    protected RequestGroupEnhancer requestGroupEnhancer;
    protected RequestEnhancer requestEnhancer;
    protected EntryEnhancer entryEnhancer;
    protected EntryFieldEnhancer entryFieldEnhancer;

    @Override
    public Module filter(EntryFilter filter) {
        entryFilter = filter;
        return this;
    }

    @Override
    public Module filter(EntryClassFilter filter) {
        entryClassFilter = filter;
        return this;
    }

    @Override
    public Module filter(RequestGroupFilter filter) {
        requestGroupFilter = filter;
        return this;
    }

    @Override
    public Module filter(RequestFilter filter) {
        requestFilter = filter;
        return this;
    }

    @Override
    public Module filter(EntryFieldFilter filter) {
        entryFieldFilter = filter;
        return this;
    }

    @Override
    public Module filter(EntryClassNameFilter filter) {
        entryClassNameFilter = filter;
        return this;
    }

    @Override
    public Module enhancer(RequestGroupEnhancer enhancer) {
        requestGroupEnhancer = enhancer;
        return this;
    }

    @Override
    public Module enhancer(RequestEnhancer enhancer) {
        requestEnhancer = enhancer;
        return this;
    }

    @Override
    public Module enhancer(EntryEnhancer enhancer) {
        entryEnhancer = enhancer;
        return this;
    }

    @Override
    public Module enhancer(EntryFieldEnhancer enhancer) {
        entryFieldEnhancer = enhancer;
        return this;
    }

    @Override
    public boolean doFilter(Context context, Class<?> entry) {
        return entryClassFilter == null || entryClassFilter.doFilter(context, entry);
    }

    @Override
    public boolean doFilter(Context context, String entryClassName) {
        return entryClassNameFilter == null || entryClassNameFilter.doFilter(context, entryClassName);
    }

    @Override
    public boolean doFilter(Context context, RequestGroup requestGroup) {
        return requestGroupFilter == null || requestGroupFilter.doFilter(context, requestGroup);
    }

    @Override
    public boolean doFilter(Context context, Request request) {
        return requestFilter == null || requestFilter.doFilter(context, request);
    }

    @Override
    public boolean doFilter(Context context, EntryField entryField) {
        return entryFieldFilter == null || entryFieldFilter.doFilter(context, entryField);
    }

    @Override
    public void enhance(Context context, RequestGroup requestGroup) {
        if (requestGroupEnhancer != null) {
            requestGroupEnhancer.enhance(context, requestGroup);
        }
    }

    @Override
    public void enhance(Context context, Request request) {
        if (requestEnhancer != null) {
            requestEnhancer.enhance(context, request);
        }
    }

    @Override
    public void enhance(Context context, Entry entry) {
        if (entryEnhancer != null) {
            entryEnhancer.enhance(context, entry);
        }
    }

    @Override
    public void enhance(Context context, EntryField entryField) {
        if (entryFieldEnhancer != null) {
            entryFieldEnhancer.enhance(context, entryField);
        }
    }

    @Override
    public int order() {
        return 0;
    }

}
