package ren.crux.rainbow.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

public class DocumentTest {

    private String json = "{\n" +
            "\t\"info\": {\n" +
            "\t\t\"_postman_id\": \"d7ea36dd-f5aa-4d3c-b25f-c5cfeb5679f4\",\n" +
            "\t\t\"name\": \"test\",\n" +
            "\t\t\"schema\": \"https://schema.getpostman.com/json/collection/v2.1.0/collection.json\"\n" +
            "\t},\n" +
            "\t\"item\": [\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"test1\",\n" +
            "\t\t\t\"item\": [\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"name\": \"req2\",\n" +
            "\t\t\t\t\t\"request\": {\n" +
            "\t\t\t\t\t\t\"method\": \"POST\",\n" +
            "\t\t\t\t\t\t\"header\": [],\n" +
            "\t\t\t\t\t\t\"url\": {\n" +
            "\t\t\t\t\t\t\t\"raw\": \"127.0.0.1:8080/dasdas/asdasd?xxx=asd\",\n" +
            "\t\t\t\t\t\t\t\"host\": [\n" +
            "\t\t\t\t\t\t\t\t\"127\",\n" +
            "\t\t\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\t\t\"1\"\n" +
            "\t\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\t\"port\": \"8080\",\n" +
            "\t\t\t\t\t\t\t\"path\": [\n" +
            "\t\t\t\t\t\t\t\t\"dasdas\",\n" +
            "\t\t\t\t\t\t\t\t\"asdasd\"\n" +
            "\t\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\t\"query\": [\n" +
            "\t\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\t\"key\": \"xxx\",\n" +
            "\t\t\t\t\t\t\t\t\t\"value\": \"asd\"\n" +
            "\t\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t\t]\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"response\": []\n" +
            "\t\t\t\t}\n" +
            "\t\t\t],\n" +
            "\t\t\t\"protocolProfileBehavior\": {}\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"req1\",\n" +
            "\t\t\t\"request\": {\n" +
            "\t\t\t\t\"method\": \"GET\",\n" +
            "\t\t\t\t\"header\": [],\n" +
            "\t\t\t\t\"url\": {\n" +
            "\t\t\t\t\t\"raw\": \"127.0.0.1:8080/dasdas/asdasd?xxx=asd\",\n" +
            "\t\t\t\t\t\"host\": [\n" +
            "\t\t\t\t\t\t\"127\",\n" +
            "\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\"1\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"port\": \"8080\",\n" +
            "\t\t\t\t\t\"path\": [\n" +
            "\t\t\t\t\t\t\"dasdas\",\n" +
            "\t\t\t\t\t\t\"asdasd\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"query\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"xxx\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"asd\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"response\": []\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"req2 Copy\",\n" +
            "\t\t\t\"request\": {\n" +
            "\t\t\t\t\"method\": \"POST\",\n" +
            "\t\t\t\t\"header\": [],\n" +
            "\t\t\t\t\"url\": {\n" +
            "\t\t\t\t\t\"raw\": \"127.0.0.1:8080/dasdas/asdasd?xxx=asd\",\n" +
            "\t\t\t\t\t\"host\": [\n" +
            "\t\t\t\t\t\t\"127\",\n" +
            "\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\"1\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"port\": \"8080\",\n" +
            "\t\t\t\t\t\"path\": [\n" +
            "\t\t\t\t\t\t\"dasdas\",\n" +
            "\t\t\t\t\t\t\"asdasd\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"query\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"xxx\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"asd\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"response\": []\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"req2 Copy Copy\",\n" +
            "\t\t\t\"request\": {\n" +
            "\t\t\t\t\"method\": \"POST\",\n" +
            "\t\t\t\t\"header\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"key\": \"Content-Type\",\n" +
            "\t\t\t\t\t\t\"name\": \"Content-Type\",\n" +
            "\t\t\t\t\t\t\"value\": \"application/x-www-form-urlencoded\",\n" +
            "\t\t\t\t\t\t\"type\": \"text\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"body\": {\n" +
            "\t\t\t\t\t\"mode\": \"urlencoded\",\n" +
            "\t\t\t\t\t\"urlencoded\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"asdasd\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"asd\",\n" +
            "\t\t\t\t\t\t\t\"type\": \"text\"\n" +
            "\t\t\t\t\t\t},\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"asd\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"asd\",\n" +
            "\t\t\t\t\t\t\t\"description\": \"asdas\",\n" +
            "\t\t\t\t\t\t\t\"type\": \"text\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"url\": {\n" +
            "\t\t\t\t\t\"raw\": \"http://localhost.local:8080/dasdas/asdasd?xxx=asd\",\n" +
            "\t\t\t\t\t\"protocol\": \"http\",\n" +
            "\t\t\t\t\t\"host\": [\n" +
            "\t\t\t\t\t\t\"localhost\",\n" +
            "\t\t\t\t\t\t\"local\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"port\": \"8080\",\n" +
            "\t\t\t\t\t\"path\": [\n" +
            "\t\t\t\t\t\t\"dasdas\",\n" +
            "\t\t\t\t\t\t\"asdasd\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"query\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"xxx\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"asd\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"response\": []\n" +
            "\t\t},\n" +
            "\t\t{\n" +
            "\t\t\t\"name\": \"req2 Copy Copy Copy\",\n" +
            "\t\t\t\"request\": {\n" +
            "\t\t\t\t\"method\": \"POST\",\n" +
            "\t\t\t\t\"header\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"key\": \"Content-Type\",\n" +
            "\t\t\t\t\t\t\"name\": \"Content-Type\",\n" +
            "\t\t\t\t\t\t\"value\": \"application/x-www-form-urlencoded\",\n" +
            "\t\t\t\t\t\t\"type\": \"text\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t],\n" +
            "\t\t\t\t\"body\": {\n" +
            "\t\t\t\t\t\"mode\": \"file\",\n" +
            "\t\t\t\t\t\"file\": {\n" +
            "\t\t\t\t\t\t\"src\": \"/Users/wangzhihui/Documents/1.json\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"url\": {\n" +
            "\t\t\t\t\t\"raw\": \"127.0.0.1:8080/dasdas/asdasd?xxx=asd\",\n" +
            "\t\t\t\t\t\"host\": [\n" +
            "\t\t\t\t\t\t\"127\",\n" +
            "\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\"0\",\n" +
            "\t\t\t\t\t\t\"1\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"port\": \"8080\",\n" +
            "\t\t\t\t\t\"path\": [\n" +
            "\t\t\t\t\t\t\"dasdas\",\n" +
            "\t\t\t\t\t\t\"asdasd\"\n" +
            "\t\t\t\t\t],\n" +
            "\t\t\t\t\t\"query\": [\n" +
            "\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\"key\": \"xxx\",\n" +
            "\t\t\t\t\t\t\t\"value\": \"asd\"\n" +
            "\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t]\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"response\": []\n" +
            "\t\t}\n" +
            "\t],\n" +
            "\t\"protocolProfileBehavior\": {}\n" +
            "}";

    @Test
    public void test() throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
//        Document document = objectMapper.readValue(json, Document.class);
//        System.out.println(document);
//        System.out.println(objectMapper.writeValueAsString(document));
//        Entry entry = new Entry();
//        Tuple tuple = new Tuple();

    }
}