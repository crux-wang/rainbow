package ren.crux.rainbow.entry.parser.filter;

import com.sun.javadoc.FieldDoc;
import lombok.NonNull;
import ren.crux.rainbow.core.parser.Context;

public interface FieldFilter {

    boolean doFilter(@NonNull Context context, @NonNull FieldDoc source);

}
