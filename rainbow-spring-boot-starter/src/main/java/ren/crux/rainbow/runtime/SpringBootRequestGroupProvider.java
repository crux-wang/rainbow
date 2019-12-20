package ren.crux.rainbow.runtime;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ren.crux.rainbow.core.Context;
import ren.crux.rainbow.core.DocumentReader;
import ren.crux.rainbow.core.RequestGroupProvider;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.core.model.RequestMethod;
import ren.crux.rainbow.core.model.RequestParam;
import ren.crux.rainbow.core.utils.EntryUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class SpringBootRequestGroupProvider implements RequestGroupProvider {

    private final RequestMappingHandlerMapping mapping;
    private DocumentReader owner;

    public SpringBootRequestGroupProvider(RequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    public static Request process(Context context, RequestMappingInfo info, HandlerMethod handlerMethod) {
        PatternsRequestCondition p = info.getPatternsCondition();
        Method method = handlerMethod.getMethod();
        String className = method.getDeclaringClass().getName();
        Type returnType = method.getGenericReturnType();
        RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
        RequestMethod[] requestMethods = methodsCondition.getMethods().stream().map(m -> RequestMethod.valueOf(m.toString())).toArray(RequestMethod[]::new);
        Request request = new Request();
        request.setName(method.getName());
        request.setType(className + "." + method.getName());
        request.setReturnType(EntryUtils.build(returnType));
        request.setMethod(requestMethods);
        request.setPath(p.getPatterns().toArray(new String[0]));
        Parameter[] parameters = method.getParameters();
        List<RequestParam> params = Arrays.stream(parameters).map(parameter -> {
            RequestParam requestParam = SpringWebHelper.process(parameter);
            context.addEntryClassName(requestParam.getType());
            return requestParam;
        }).collect(Collectors.toList());
        request.setParams(params);
        return request;
    }

    @Override
    public List<RequestGroup> get(Context context) {
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        Map<String, RequestGroup> groupMap = new HashMap<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo info = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            Request request = process(context, info, handlerMethod);
            Method method = handlerMethod.getMethod();
            String className = method.getDeclaringClass().getName();
            RequestGroup requestGroup = groupMap.get(className);
            if (requestGroup == null) {
                requestGroup = new RequestGroup();
                requestGroup.setName(method.getDeclaringClass().getSimpleName());
                requestGroup.setType(className);
                requestGroup.setPath(SpringWebHelper.getRequestPath(className));
                groupMap.put(className, requestGroup);
            }
            requestGroup.addRequest(request);
        }
        return new LinkedList<>(groupMap.values());
    }


    @Override
    public void owner(DocumentReader reader) {
        owner = reader;
    }

    @Override
    public DocumentReader end() {
        return owner;
    }
}
