package ren.crux.rainbow.core;

import ren.crux.rainbow.core.module.Context;
import ren.crux.raonbow.common.model.RequestGroup;

import java.util.List;

public interface RequestGroupProvider {

    List<RequestGroup> get(Context context);

}
