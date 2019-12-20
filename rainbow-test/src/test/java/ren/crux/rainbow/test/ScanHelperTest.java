package ren.crux.rainbow.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ren.crux.rainbow.core.ClassDescProvider;
import ren.crux.rainbow.core.DefaultClassDescProvider;
import ren.crux.rainbow.core.DefaultDocumentReader;
import ren.crux.rainbow.core.RequestGroupProvider;
import ren.crux.rainbow.core.model.Document;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ScanHelperTest {

    @Autowired
    private RequestGroupProvider requestGroupProvider;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void name() throws Exception {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-test/src/main/java/";
        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String[] packageNames = new String[]{"ren.crux.rainbow.test.demo"};
        ClassDescProvider classDescProvider = new DefaultClassDescProvider();

        Document document = new DefaultDocumentReader()
                .with(classDescProvider)
                .with(requestGroupProvider)
                .useDefaultModule()
                .impl("org.springframework.data.domain.Page", "org.springframework.data.domain.PageImpl")
                .impl("org.springframework.data.domain.Pageable", "org.springframework.data.domain.PageRequest")
                .cdp()
                .useDefaultFilter()
                .source(path)
                .packages(packageNames)
                .end()
                .read().orElse(null);
        System.out.println(objectMapper.writeValueAsString(document));


    }
}