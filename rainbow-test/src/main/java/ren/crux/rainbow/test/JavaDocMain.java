package ren.crux.rainbow.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections4.CollectionUtils;
import ren.crux.rainbow.core.parser.impl.DefaultRootDocParser;
import ren.crux.rainbow.core.reader.JavaDocReader;
import ren.crux.rainbow.core.reader.impl.DefaultJavaDocReader;
import ren.crux.rainbow.core.reader.parser.RootDocParser;

import java.util.List;


/**
 * @author wangzhihui
 */
public class JavaDocMain {

    public static void main(String[] args) throws Exception {
//        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-test/src/main/java/";
//        final String path = "/Users/wangzhihui/workspace/project/haoda-service/haoda-common/src/main/java/";
//        final String path = "/Users/wangzhihui/.m2/repository/org/springframework/data/spring-data-commons/2.1.10.RELEASE/spring-data-commons-2.1.10.RELEASE-sources.jar";
        final String[] packageNames = new String[]{"ren.crux.rainbow.test.demo"};
//        final String[] packageNames = new String[]{"org.springframework.data.domain"};
        RootDocParser<Document> rootParser = new DefaultRootDocParser();
        JavaDocReader<Document> javaDocReader = new DefaultJavaDocReader<>(rootParser);
        Document document = javaDocReader.read(path, packageNames).orElseThrow(Exception::new);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        System.out.println(objectMapper.writeValueAsString(document));
        System.out.println("entry size : " + CollectionUtils.size(document.getEntryMap()));
        List<RequestGroup> requestGroups = document.getRequestGroups();
        System.out.println("request group size : " + CollectionUtils.size(requestGroups));
        if (requestGroups != null) {
            int requestSize = 0;
            for (RequestGroup requestGroup : requestGroups) {
                int size = CollectionUtils.size(requestGroup.getRequests());
                requestSize += size;
                System.out.println(requestGroup.getName() + " : " + size);
            }
            System.out.println("request size : " + requestSize);
        }
    }
}
