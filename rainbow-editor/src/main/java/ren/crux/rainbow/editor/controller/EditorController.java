package ren.crux.rainbow.editor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestGroup;
import ren.crux.rainbow.editor.model.DocsListUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

/**
 * EditorController
 *
 * @author wangzhihui
 **/
@RestController
@RequestMapping("/editor")
public class EditorController {

    private static final String DOCS_ID_COOKIES_KEY = "DOCS_ID";

    private final Cache<String, Document> documentCache = CacheBuilder.newBuilder().build();
    private final ObjectMapper objectMapper;

    public EditorController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/load")
    public Document load(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException {
        String json = IOUtils.toString(file.getInputStream(), StandardCharsets.UTF_8);
        Document document = objectMapper.readValue(json, Document.class);
        String id = UUID.randomUUID().toString();
        id = "test";
        documentCache.put(id, document);
        response.addCookie(new Cookie(DOCS_ID_COOKIES_KEY, id));
        return DocsListUtils.form(document);
    }

    @GetMapping("/source")
    public Document source(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId) {
        return documentCache.getIfPresent(docsId);
    }

    @GetMapping("/docs")
    public Document docs(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId) {
        return DocsListUtils.form(documentCache.getIfPresent(docsId));
    }

    @GetMapping("/groups/{idx}")
    public RequestGroup requestGroup(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @PathVariable int idx) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        return DocsListUtils.form(document.getRequestGroups().get(idx));
    }

    @PostMapping("/groups/{idx}")
    public RequestGroup updateRequestGroup(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @PathVariable int idx, @RequestBody RequestGroup body) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        RequestGroup requestGroup = document.getRequestGroups().get(idx);
        if (requestGroup != null) {
            requestGroup.setCommentText(body.getCommentText());
        }
        return DocsListUtils.form(requestGroup);
    }

    @PostMapping("/groups")
    public RequestGroup updateRequestGroup(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @RequestBody RequestGroup body) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        document.addRequestGroup(body);
        return DocsListUtils.form(body);
    }

    @GetMapping("/groups/{idx}/requests/{subIdx}")
    public Request request(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @PathVariable int idx, @PathVariable int subIdx) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        return document.getRequestGroups().get(idx).getRequests().get(subIdx);
    }

    @PostMapping("/groups/{idx}/requests/{subIdx}")
    public Request updateRequest(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @PathVariable int idx, @PathVariable int subIdx, @RequestBody Request body) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        List<Request> requests = document.getRequestGroups().get(idx).getRequests();
        requests.set(subIdx, body);
        return body;
    }

    @PostMapping("/groups/{idx}/requests")
    public Request updateRequest(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @PathVariable int idx, @RequestBody Request body) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        List<Request> requests = document.getRequestGroups().get(idx).getRequests();
        requests.add(body);
        return body;
    }


    @GetMapping("/entries/{name}")
    public Entry entry(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @PathVariable String name) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        return document.getEntryMap().get(name);
    }

    @GetMapping("/entries")
    public Entry entry(@CookieValue(DOCS_ID_COOKIES_KEY) String docsId, @RequestBody Entry body) {
        Document document = documentCache.getIfPresent(docsId);
        if (document == null) {
            return null;
        }
        return document.getEntryMap().put(body.getType(), body);
    }

}
