package ren.crux.rainbow.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.parser.Context;
import ren.crux.rainbow.core.reader.JavaDocReader;
import ren.crux.rainbow.core.reader.impl.DefaultJavaDocReader;
import ren.crux.rainbow.entry.EntrySupportContext;

/**
 * @author wangzhihui
 */
public class JavaDocMain {

    public static void main(String[] args) throws JsonProcessingException {
        final String path = "D:\\workspace\\github\\rainbow\\rainbow-test\\src\\main\\java\\";
        final String[] packageNames = new String[]{"ren.crux.rainbow.test.demo"};
        JavaDocReader javaDocReader = new DefaultJavaDocReader() {
            @Override
            protected Context newContext() {
                return new EntrySupportContext(getRootDoc());
            }
        };
        Document document = javaDocReader.read(path, packageNames).orElse(null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        System.out.println(objectMapper.writeValueAsString(document));

    }
}
