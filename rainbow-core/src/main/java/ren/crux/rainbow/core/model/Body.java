package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.entry.Entry;
import ren.crux.rainbow.core.entry.Tuple;

import java.util.List;

@Data
public class Body {
    private String mode;
    private String row;
    private Entry entry;
    private List<Tuple> formdata;
    private List<Tuple> urlencoded;
    private Tuple file;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mode", mode)
                .add("row", row)
                .add("entry", entry)
                .add("formdata", formdata)
                .add("urlencoded", urlencoded)
                .add("file", file)
                .omitNullValues()
                .toString();
    }
}
