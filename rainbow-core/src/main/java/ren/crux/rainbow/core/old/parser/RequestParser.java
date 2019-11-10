package ren.crux.rainbow.core.old.parser;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.MethodDoc;
import org.apache.commons.lang3.tuple.Pair;
import ren.crux.rainbow.core.old.model.Body;
import ren.crux.rainbow.core.old.model.Request;
import ren.crux.rainbow.core.old.model.Requests;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.parser.JavaDocParser;

import java.util.List;
import java.util.Optional;

/**
 * @author wangzhihui
 */
public class RequestParser implements JavaDocParser<MethodDoc, Requests> {

    @Override
    public boolean condition(Context context, MethodDoc source) {
        return source.isMethod() && context.isRequest(source);
    }

    @Override
    public Requests parse(Context context, MethodDoc source) {
        Requests requests = new Requests();
        String superPath = "";
        Optional<Pair<List<String>, List<String>>> optional = context.getRequestMappingPathAndMethod(source.containingClass());
        Body body = context.parseParameter(source);
        System.out.println("body = " + body);
        List<AnnotationDesc> requestAnnotations = context.getRequestAnnotations(source);
        for (AnnotationDesc requestAnnotation : requestAnnotations) {
            Request request = new Request();
            requests.addRequest(request);
        }
        return requests;
    }
}
