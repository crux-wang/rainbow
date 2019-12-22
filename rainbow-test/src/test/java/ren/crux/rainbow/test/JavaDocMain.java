package ren.crux.rainbow.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ren.crux.rainbow.core.AbstractClassDocProvider;
import ren.crux.rainbow.core.ClassDocProvider;
import ren.crux.rainbow.core.Context;
import ren.crux.rainbow.javadoc.model.ClassDesc;

import java.util.Map;


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
//        final String[] packageNames = new String[]{"com.xiaomi.mig3.haoda.common.model"};
//        RootDocParser<List<ClassDesc>> rootParser = new DefaultRootDocParser();
//        JavaDocReader<List<ClassDesc>> javaDocReader = new DefaultJavaDocReader(rootParser);
//        List<ClassDesc> classDescList = javaDocReader.read(path, packageNames).orElseThrow(Exception::new);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        System.out.println(objectMapper.writeValueAsString(classDescList));
//        System.out.println("classDescList.size() = " + classDescList.size());
        ClassDocProvider classDocProvider = new AbstractClassDocProvider()
                .useDefaultFilter().source(path).packages(packageNames);
        Context context = new Context();
        classDocProvider.setUp(context);
        Map<String, ClassDesc> map = classDocProvider.all();
        System.out.println(objectMapper.writeValueAsString(map));
    }
}
