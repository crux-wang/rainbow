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
public class Request {

    private Url url;
    private String method;
    private List<Tuple> header = new LinkedList<>();
    private Body body;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("method", method)
                .add("header", header)
                .add("body", body)
                .omitNullValues()
                .toString();
    }
}
