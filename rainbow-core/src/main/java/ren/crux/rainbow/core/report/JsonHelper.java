package ren.crux.rainbow.core.report;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author wangzhihui
 */
public class JsonHelper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.registerModule(new GuavaModule());
    }

    public static String writeValueAsString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toBean(String json, Class<T> type) throws IOException {
        return objectMapper.readValue(json, type);
    }

    public static <T> T toBean(JsonNode json, TypeReference<T> type) throws IOException {
        return toBean(json, type, null);
    }

    public static <T> T toBean(JsonNode json, TypeReference<T> type, T defaultVal) throws IOException {
        if (json == null) {
            return defaultVal;
        } else {
            T value = objectMapper.readValue(json.toString(), type);
            return value == null ? defaultVal : value;
        }
    }

    public static <T> T toBean(String json, TypeReference<T> type) throws IOException {
        return toBean(json, type, null);
    }

    public static <T> T toBean(String json, TypeReference<T> type, T defaultVal) throws IOException {
        if (StringUtils.isBlank(json)) {
            return defaultVal;
        } else {
            T value = objectMapper.readValue(json, type);
            return value == null ? defaultVal : value;
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}

