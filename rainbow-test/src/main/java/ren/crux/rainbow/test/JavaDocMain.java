package ren.crux.rainbow.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.javadoc.RootDoc;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.reader.JavaDocReader;
import ren.crux.rainbow.core.reader.impl.DefaultJavaDocReader;
import ren.crux.rainbow.request.RootParser;
import ren.crux.rainbow.request.model.RequestGroups;

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
        RootParser rootParser = new RootParser();
        RequestGroups requestGroups = rootParser.parse(new Context(JavaDocReader.Doclet.getRootDoc()), rootDoc).orElseThrow(Exception::new);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        System.out.println(objectMapper.writeValueAsString(requestGroups));

    }
}
