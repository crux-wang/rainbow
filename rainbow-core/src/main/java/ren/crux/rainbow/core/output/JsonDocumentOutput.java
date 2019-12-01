package ren.crux.rainbow.core.output;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ren.crux.rainbow.core.model.Document;

/**
 * json 格式文档输出
 *
 * @author wangzhihui
 */
public class JsonDocumentOutput implements DocumentOutput<String> {

    private ObjectMapper objectMapper;

    public JsonDocumentOutput() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    /**
     * 写入
     *
     * @param document 文档
     * @return <T>
     */
    @Override
    public String write(Document document) {
        try {
            return objectMapper.writeValueAsString(document);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
