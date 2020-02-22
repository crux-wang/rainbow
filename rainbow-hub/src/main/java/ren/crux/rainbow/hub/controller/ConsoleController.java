package ren.crux.rainbow.hub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ren.crux.rainbow.hub.service.DocumentService;
import ren.crux.raonbow.common.model.Document;
import ren.crux.raonbow.common.model.Request;
import ren.crux.raonbow.common.model.RequestGroup;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * ConsoleController
 *
 * @author wangzhihui
 **/
@RestController
@RequestMapping("/console")
public class ConsoleController {

    private final DocumentService documentService;
    private final ObjectMapper objectMapper;

    public ConsoleController(DocumentService documentService, ObjectMapper objectMapper) {
        this.documentService = documentService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/source/deploy/{id}")
    public void deploy(@PathVariable String id, @RequestParam MultipartFile file) throws IOException {
        File localFile = new File("/tmp/" + id + ".json");
        FileUtils.copyInputStreamToFile(file.getInputStream(), localFile);
        String json = FileUtils.readFileToString(localFile, "utf8");
        documentService.add(id, objectMapper.readValue(json, Document.class));
    }

    @GetMapping("/source/{id}")
    public Document get(@PathVariable String id) {
        return documentService.get(id).orElse(null);
    }

    @GetMapping("/source/{id}/groups")
    public List<RequestGroup> groups(@PathVariable String id) {
        return documentService.get(id).map(Document::getRequestGroups).orElse(Collections.emptyList());
    }

    @GetMapping("/source/{id}/groups/{order}")
    public RequestGroup groups(@PathVariable String id, @PathVariable int order) {
        return documentService.get(id).map(document -> {
            List<RequestGroup> requestGroups = document.getRequestGroups();
            return requestGroups.size() > order ? requestGroups.get(order) : null;
        }).orElse(null);
    }

    @GetMapping("/source/{id}/groups/{order}/{subOrder}")
    public Request groups(@PathVariable String id, @PathVariable int order, @PathVariable int subOrder) {
        return documentService.get(id).map(document -> {
            List<RequestGroup> requestGroups = document.getRequestGroups();
            if (requestGroups.size() > order) {
                RequestGroup requestGroup = requestGroups.get(order);
                List<Request> requests = requestGroup.getRequests();
                if (requests.size() > subOrder) {
                    return requests.get(subOrder);
                }
            }
            return null;
        }).orElse(null);
    }
}
