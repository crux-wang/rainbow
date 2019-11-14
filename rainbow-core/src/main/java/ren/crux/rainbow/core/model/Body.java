package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class Body {
    private String mode;
    private String row;
    private Link entryLink;
    private List<Field> formdata = new LinkedList<>();
    private List<Field> urlencoded = new LinkedList<>();
    private Field file;

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
