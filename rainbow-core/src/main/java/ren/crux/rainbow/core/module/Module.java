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

import java.util.stream.Collectors;

public interface Module {

    Module filter(EntryFilter filter);

    Module filter(RequestGroupFilter filter);

    Module filter(RequestFilter filter);

    Module filter(EntryFieldFilter filter);

    Module filter(EntryClassNameFilter filter);

    Module filter(EntryClassFilter filter);

    Module enhancer(RequestGroupEnhancer enhancer);

    Module enhancer(RequestEnhancer enhancer);

    Module enhancer(EntryEnhancer enhancer);

    Module enhancer(EntryFieldEnhancer enhancer);

    boolean doFilter(Context context, Class<?> entry);

    boolean doFilter(Context context, String entryClassName);

    boolean doFilter(Context context, RequestGroup requestGroup);

    boolean doFilter(Context context, Request request);

    boolean doFilter(Context context, EntryField entryField);

    void enhance(Context context, RequestGroup requestGroup);

    void enhance(Context context, Request request);

    void enhance(Context context, Entry entry);

    void enhance(Context context, EntryField entryField);

    default boolean doFilterAndEnhance(Context context, RequestGroup requestGroup) {
        if (doFilter(context, requestGroup)) {
            enhance(context, requestGroup);
            requestGroup.setRequests(requestGroup.getRequests().stream().filter(r -> doFilterAndEnhance(context, r)).collect(Collectors.toList()));
            return true;
        }
        return false;
    }

    default boolean doFilterAndEnhance(Context context, Request request) {
        if (doFilter(context, request)) {
            enhance(context, request);
            return true;
        }
        return false;
    }

    default boolean doFilterAndEnhance(Context context, EntryField entryField) {
        if (doFilter(context, entryField)) {
            enhance(context, entryField);
            return true;
        }
        return false;
    }

    String getName();

    int order();

}
