package ren.crux.rainbow.core.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ren.crux.rainbow.core.model.Document;

import java.util.Optional;

public class JsonReporter implements Reporter<String> {

    public static final JsonReporter INSTANCE = new JsonReporter();

    private final ObjectMapper objectMapper;

    public JsonReporter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    public JsonReporter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> report(Document document) {
        return Optional.ofNullable(document).map(d -> {
            try {
                return objectMapper.writeValueAsString(d);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
