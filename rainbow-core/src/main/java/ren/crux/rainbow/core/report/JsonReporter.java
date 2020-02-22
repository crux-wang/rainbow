package ren.crux.rainbow.core.report;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ren.crux.raonbow.common.model.Document;

import java.util.Optional;

public class JsonReporter implements Reporter<String> {

    public static final JsonReporter INSTANCE = new JsonReporter();

    private final ObjectMapper objectMapper;

    public JsonReporter() {
       this.objectMapper = JsonHelper.getObjectMapper();
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
