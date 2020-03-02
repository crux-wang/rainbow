package ren.crux.rainbow.openapi3;

import com.github.therapi.runtimejavadoc.Comment;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;


@Component
public class RainbowOperationCustomizer implements OperationCustomizer {
    /**
     * @param operation     input operation
     * @param handlerMethod original handler method
     * @return customized operation
     */
    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        MethodJavadoc javadoc = RuntimeJavadocUtils.getJavadoc(handlerMethod.getMethod());
        Comment comment = javadoc.getComment();
        operation.setSummary(RuntimeJavadocUtils.summary(comment));
        operation.setDescription(RuntimeJavadocUtils.format(comment));
        return operation;
    }
}
