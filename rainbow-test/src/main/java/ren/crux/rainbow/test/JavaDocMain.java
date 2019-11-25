package ren.crux.rainbow.test;

import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.reader.impl.DefaultJavaDocReader;

/**
 * @author wangzhihui
 */
public class JavaDocMain {

    public static void main(String[] args) throws Exception {
//        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String path = "/Users/wangzhihui/workspace/project/rainbow/rainbow-test/src/main/java/";
        final String[] packageNames = new String[]{"ren.crux.rainbow.test.demo"};
        DefaultJavaDocReader javaDocReader = new DefaultJavaDocReader();
        RootDoc rootDoc = javaDocReader.read(path, packageNames).orElseThrow(Exception::new);
//        RootParser rootParser = new RootParser();
//        Map<String, Entry> entryMap = rootParser.parse(new Context(JavaDocReader.Doclet.getRootDoc()), rootDoc).orElseThrow(Exception::new);
//        RootParser rootParser
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        System.out.println(objectMapper.writeValueAsString(entryMap));

    }
}
