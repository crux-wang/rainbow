package ren.crux.rainbow.core.module.filter;

import ren.crux.rainbow.core.Context;
import ren.crux.rainbow.core.model.RequestGroup;

public class DefaultRequestGroupFilter implements RequestGroupFilter {
    @Override
    public boolean doFilter(Context context, RequestGroup requestGroup) {
        if ("org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController".equals(requestGroup.getType())) {
            return false;
        }
        return true;
    }
}
