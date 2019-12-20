package ren.crux.rainbow.core.module.filter;

import ren.crux.rainbow.core.Context;
import ren.crux.rainbow.core.model.EntryField;

public class DefaultEntryFieldFilter implements EntryFieldFilter {

    @Override
    public boolean doFilter(Context context, EntryField entryField) {
        if ("serialVersionUID".equals(entryField.getName()) && "long".equals(entryField.getType().getType())) {
            return false;
        }
        return true;
    }

}
