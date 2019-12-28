package ren.crux.rainbow.runtime;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import ren.crux.rainbow.core.RequestGroupProvider;

/**
 * @author wangzhihui
 */
@Configuration
@ConditionalOnWebApplication
public class RainbowAutoConfiguration {

    @Bean
    @ConditionalOnBean(RequestMappingHandlerMapping.class)
    public RequestGroupProvider springBootRequestGroupProvider(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        return new SpringBootRequestGroupProvider(requestMappingHandlerMapping);
    }

}
