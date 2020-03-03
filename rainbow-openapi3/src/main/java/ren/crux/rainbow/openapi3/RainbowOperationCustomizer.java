/*
 *  Copyright 2020. The Crux Authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ren.crux.rainbow.openapi3;

import com.github.therapi.runtimejavadoc.Comment;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import ren.crux.rainbow.common.utils.RuntimeJavadocUtils;

import java.math.BigDecimal;
import java.util.Arrays;


@Component
public class RainbowOperationCustomizer implements OperationCustomizer {

    @Autowired
    private SpringDataWebProperties springDataWebProperties;

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
        Arrays.stream(handlerMethod.getMethodParameters())
                .filter(mp -> StringUtils.equals("org.springframework.data.domain.Pageable", mp.getParameterType().getName()))
                .findFirst().ifPresent(mp -> {
            operation.addParametersItem(page());
            operation.addParametersItem(pageSize());
            operation.getParameters().removeIf(p ->
                    BooleanUtils.isTrue(p.getDeprecated())
                            && StringUtils.equals("query", p.getIn())
                            && p.getSchema() != null
                            && StringUtils.equals("#/components/schemas/Pageable", p.getSchema().get$ref()));
        });
        return operation;
    }

    private Parameter pageSize() {
        SpringDataWebProperties.Pageable pageable = springDataWebProperties.getPageable();
        Schema pageSizeSchema = new NumberSchema()
                .type("integer")
                .format("int32")
                .minimum(new BigDecimal(0))
                .maximum(new BigDecimal(pageable.getMaxPageSize()));
        pageSizeSchema.setDefault(pageable.getDefaultPageSize());
        Parameter pageSizeParam = new Parameter()
                .description("数据条数")
                .name(pageable.getSizeParameter())
                .in("query")
                .required(true)
                .schema(pageSizeSchema);
        return pageSizeParam;
    }

    private Parameter page() {
        SpringDataWebProperties.Pageable pageable = springDataWebProperties.getPageable();
        return new Parameter()
                .description("页码")
                .name(pageable.getPageParameter())
                .in("query")
                .required(true)
                .schema(
                        new NumberSchema()
                                .type("integer")
                                .format("int32")
                                .minimum(new BigDecimal(pageable.isOneIndexedParameters() ? 1 : 0))
                );
    }
}
