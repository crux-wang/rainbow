package ren.crux.rainbow.core;

import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.module.Context;

import java.util.List;

public interface RequestGroupProvider {

    default void owner(DocumentReader reader) {

    }

    default DocumentReader end() {
        return null;
    }

    List<RequestGroup> get(Context context);

}
