package ren.crux.rainbow.core.old.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.model.Link;
import ren.crux.rainbow.core.model.Tuple;

import java.util.List;

@Data
public class Body {
    private String mode;
    private String row;
    private Link entryLink;
    private List<Tuple> formdata;
    private List<Tuple> urlencoded;
    private Tuple file;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mode", mode)
                .add("row", row)
                .add("entryLink", entryLink)
                .add("formdata", formdata)
                .add("urlencoded", urlencoded)
                .add("file", file)
                .omitNullValues()
                .toString();
    }
}
