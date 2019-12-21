package ren.crux.rainbow.core.report;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.*;
import ren.crux.rainbow.javadoc.model.CommentText;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class HtmlReporter implements Reporter<String> {

    public static final String DOCUMENT_TEMPLATE = "document-template.html";
    public static final String REQUEST_GROUP_TEMPLATE = "request-group-template.html";
    public static final String REQUEST_TEMPLATE = "request-template.html";
    public static final String METHOD_PATH_LINE_TEMPLATE = "method-path-line-template.html";
    public static final String METHOD_TEMPLATE = "method-template.html";
    public static final String PATH_TEMPLATE = "path-template.html";
    public static final String REQUEST_PARAM_TEMPLATE = "request-param-template.html";
    public static final String ENTRY_TEMPLATE = "entry-template.html";
    public static final String ENTRY_FIELD_TEMPLATE = "entry-field-template.html";


    private Cache<String, String> cache = CacheBuilder.newBuilder().build();

    @Override
    public Optional<String> report(Document document) {
        if (document == null) {
            return Optional.empty();
        }
        String template = getTemplate(DOCUMENT_TEMPLATE);

        String html = StringUtils.replaceEach(template,
                new String[]{
                        "${request-group-list-template}",
                        "${entry-list-template}",
                },
                new String[]{
                        report(document.getRequestGroups()),
                        ""
                });
        return Optional.of(html);
    }

    private String report(List<RequestGroup> requestGroups) {
        StringBuilder sb = new StringBuilder();
        for (RequestGroup requestGroup : requestGroups) {
            sb.append(report(requestGroup));
        }
        return sb.toString();
    }

    private String report(RequestGroup requestGroup) {
        String template = getTemplate(REQUEST_GROUP_TEMPLATE);
        CommentText commentText = requestGroup.getCommentText();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}", "${type}", "${path}",
                        "${request-list-template}"},
                new String[]{
                        requestGroup.getName(),
                        requestGroup.getType(),
                        ArrayUtils.toString(requestGroup.getPath()),
                        reportRequests(requestGroup.getRequests())
                });
        return replace(template, commentText);
    }

    private String replace(String template, CommentText commentText) {
        return StringUtils.replaceEach(template,
                new String[]{
                        "${commentText.text}",
                        "${commentText.firstLine}",
                        "${commentText.content}",
                        "${commentText.inline}",
                },
                new String[]{
                        commentText == null ? "" : defaultString(commentText.getText()),
                        commentText == null ? "" : defaultString(commentText.firstLine()),
                        commentText == null ? "" : defaultString(commentText.content()),
                        commentText == null ? "" : defaultString(commentText.inline())
                });
    }

    private String replace(String template, TypeDesc type) {
        return StringUtils.replaceEach(template,
                new String[]{
                        "${type.name}",
                        "${type.simpleName}",
                        "${type.type}",
                        "${type.type-id}",
                        "${returnType.name}",
                        "${returnType.simpleName}",
                        "${returnType.type}",
                        "${returnType.type-id}"
                },
                new String[]{
                        type == null ? "" : defaultString(type.getName()),
                        type == null ? "" : defaultString(type.getSimpleName()),
                        type == null ? "" : defaultString(type.getType()),
                        type == null ? "" : buildId(type.getType()),
                        type == null ? "" : defaultString(type.getName()),
                        type == null ? "" : defaultString(type.getSimpleName()),
                        type == null ? "" : defaultString(type.getType()),
                        type == null ? "" : buildId(type.getType()),
                });
    }

    private String buildId(String type) {
        return defaultString(StringUtils.replace(type, ".", "-"));
    }


    private String reportRequests(List<Request> requests) {
        StringBuilder result = new StringBuilder();
        for (Request request : requests) {
            result.append(report(request));
        }
        return result.toString();
    }

    private String report(Request request) {
        String template = getTemplate(REQUEST_TEMPLATE);
        CommentText commentText = request.getCommentText();
        TypeDesc returnType = request.getReturnType();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}", "${type}", "${type-id}", "${path}",
                        "${method}",
                        "${returnCommentText}",
                        "${method-path-list-template}",
                        "${request-param-list-template}"
                },
                new String[]{
                        request.getName(),
                        request.getType(),
                        buildId(request.getType()),
                        ArrayUtils.toString(request.getPath()),
                        ArrayUtils.toString(request.getMethod()),
                        request.getReturnCommentText(),
                        report(request.getMethod(), request.getPath()),
                        reportRequestParams(request.getParams())
                });
        template = replace(template, commentText);
        return replace(template, returnType);
    }

    private String reportRequestParams(List<RequestParam> requestParams) {
        StringBuilder sb = new StringBuilder();
        for (RequestParam requestParam : requestParams) {
            sb.append(report(requestParam));
        }
        return sb.toString();
    }

    private String report(RequestParam requestParam) {
        String template = getTemplate(REQUEST_PARAM_TEMPLATE);
        CommentText commentText = requestParam.getCommentText();
        TypeDesc type = requestParam.getType();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}",
                        "${required}",
                        "${defaultValue}",
                        "${paramType}"
                },
                new String[]{
                        requestParam.getName(),
                        requestParam.isRequired() ? "*" : "",
                        requestParam.getDefaultValue(),
                        requestParam.getParamType().name()
                });
        template = replace(template, commentText);
        return replace(template, type);
    }

    private String report(RequestMethod[] method, String[] path) {
        String template = getTemplate(METHOD_PATH_LINE_TEMPLATE);
        return StringUtils.replaceEach(template,
                new String[]{
                        "${method-list-template}",
                        "${path-list-template}"
                },
                new String[]{
                        reportRequestMethods(method),
                        reportPaths(path)
                });
    }

    private String reportRequestMethods(RequestMethod[] method) {
        String template = getTemplate(METHOD_TEMPLATE);
        StringBuilder sb = new StringBuilder();
        for (RequestMethod requestMethod : method) {
            sb.append(StringUtils.replace(template, "${method}", requestMethod.name()));
        }
        return sb.toString();
    }

    private String reportPaths(String[] paths) {
        String template = getTemplate(PATH_TEMPLATE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.length; i++) {
            sb.append(StringUtils.replace(template, "${path}", paths[i]));
            if (i < paths.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

//    private String reportEntries(List<Entry> entries) {
//
//    }
//
//    private String report(Entry entry) {
//
//    }
//
//    private String report(EntryField entryField) {
//
//    }

    private String getTemplate(String name) {
        try {
            return cache.get(name, () -> FileUtils.readFileToString(new File(Objects.requireNonNull(getClass().getClassLoader().getResource(name)).getFile()), "utf8"));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
