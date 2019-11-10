package ren.crux.rainbow.core.model;

import com.google.common.base.MoreObjects;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangzhihui
 */
@Data
public class Request {

    private Url url = new Url();
    private String method;
    private List<Tuple> header = new LinkedList<>();
    private Body body = new Body();

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
