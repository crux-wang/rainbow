package ren.crux.rainbow.test.demo;

import com.google.common.base.Predicates;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ren.crux.rainbow.swagger2.data.RainbowSpringDataRestConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * RainbowTestApplication
 *
 * @author wangzhihui
 **/
@SpringBootApplication
@ComponentScan(basePackages = "ren.crux.rainbow")
public class RainbowTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainbowTestApplication.class, args);
    }

    @Configuration
    @EnableSwagger2
    @Import(RainbowSpringDataRestConfiguration.class)
    public class SwaggerConfig {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .forCodeGeneration(true)
                    .useDefaultResponseMessages(false)
                    .select()
                    .apis(RequestHandlerSelectors.any())
                    .paths(Predicates.not(PathSelectors.regex("/error.*")))
                    .build();
        }
    }
}
