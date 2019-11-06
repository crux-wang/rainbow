package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;
import ren.crux.rainbow.core.entry.Tuple;

import java.util.List;

@Data
public class Url {

    private String raw;
    private List<String> host;
    private String protocol;
    private String port;
    private List<String> path;
    private List<Tuple> query;

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
