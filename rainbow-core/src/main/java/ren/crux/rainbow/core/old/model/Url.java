package ren.crux.rainbow.core.old.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.model.Tuple;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class Url {

    private String raw;
    private List<String> host = new LinkedList<>();
    private String protocol;
    private String port;
    private List<String> path = new LinkedList<>();
    private List<Tuple> query = new LinkedList<>();

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("raw", raw)
                .add("host", host)
                .add("protocol", protocol)
                .add("port", port)
                .add("path", path)
                .add("query", query)
                .omitNullValues()
                .toString();
    }
}
