package ren.crux.rainbow.test.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.models.Swagger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.service.Documentation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.spring.web.json.JsonSerializer;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RainbowTestApplicationTest {

    @Autowired
    private DocumentationCache documentationCache;
    @Autowired
    private ServiceModelToSwagger2Mapper mapper;
    @Autowired
    private JsonSerializer jsonSerializer;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void main() throws JsonProcessingException {
        Documentation documentation = documentationCache.documentationByGroup(Docket.DEFAULT_GROUP_NAME);
        Swagger swagger = mapper.mapDocumentation(documentation);
        Json json = jsonSerializer.toJson(swagger);
        System.out.println(json.value());
    }
}