package ren.crux.rainbow.runtime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void name() throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-spring-boot-starter/src/test/java/";
        final String[] packageNames = new String[]{"ren.crux.rainbow.runtime.demo"};
        System.out.println(objectMapper.writeValueAsString(DocHelper.read(requestMappingHandlerMapping, path, packageNames)));
    }
}