package ren.crux.rainbow.core.report.html;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.DefaultClassDocProvider;
import ren.crux.raonbow.common.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TemplateHtmlReporter extends HtmlReporter {

    public static final TemplateHtmlReporter INSTANCE = new TemplateHtmlReporter("/tmp/");

    public static final String DOCUMENT_TEMPLATE = "document-template.html";
    public static final String REQUEST_GROUP_TEMPLATE = "request-group-template.html";
    public static final String REQUEST_GROUP_ITEM_TEMPLATE = "request-group-item-template.html";
    public static final String REQUEST_TEMPLATE = "request-template.html";
    public static final String METHOD_PATH_LINE_TEMPLATE = "method-path-line-template.html";
    public static final String METHOD_TEMPLATE = "method-template.html";
    public static final String PATH_TEMPLATE = "path-template.html";
    public static final String REQUEST_PARAM_TEMPLATE = "request-param-template.html";
    public static final String REQUEST_PARAM_GROUP_TEMPLATE = "request-param-group-template.html";
    public static final String ENTRY_TEMPLATE = "entry-template.html";
    public static final String ENTRY_FIELD_TEMPLATE = "entry-field-template.html";


    protected TemplateHtmlReporter(String dirPath) {
        super(dirPath);
    }

    protected String getTemplate(String name) {
        if (function != null) {
            return function.apply(name);
        }
        try {
            return cache.get(name, () -> FileUtils.readFileToString(new File(Objects.requireNonNull(TemplateHtmlReporter.class.getClassLoader().getResource(name)).getFile()), "utf8"));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Map<String, File>> report(Document document) {
        if (document == null) {
            return Optional.empty();
        }
        String template = getTemplate(DOCUMENT_TEMPLATE);
        String home = StringUtils.replaceEach(template,
                new String[]{
                        "${source}",
                        "${packages}",
                        "${request-group-list-template}",
                },
                new String[]{
                        Arrays.toString((String[]) document.getProperties().get(DefaultClassDocProvider.SOURCE_PATH.getName())),
                        Arrays.toString((String[]) document.getProperties().get(DefaultClassDocProvider.PACKAGES.getName())),
                        reportRequestGroups(document.getRequestGroups()),
                });
        Map<String, File> map = new HashMap<>();
        map.put("index.html", writeStringToFile("index.html", home));

        for (RequestGroup requestGroup : document.getRequestGroups()) {
            String fileName = "rg-" + buildId(requestGroup.getType()) + ".html";
            map.put(fileName, writeStringToFile(fileName, report(document, requestGroup)));
        }
        if (map.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(map);
        }
    }

    private String buildId(String type) {
        return StringUtils.replaceEach(type, new String[]{".", "$"}, new String[]{"-", "_"});
    }

    private File writeStringToFile(String name, String content) {
        File file = new File(dirPath + name);
        try {
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private String reportRequestGroups(List<RequestGroup> requestGroups) {
        StringBuilder sb = new StringBuilder();
        for (RequestGroup requestGroup : requestGroups) {
            sb.append(reportRequestGroupItem(requestGroup));
        }
        return sb.toString();
    }

    private String reportRequestGroupItem(RequestGroup requestGroup) {
        String template = getTemplate(REQUEST_GROUP_ITEM_TEMPLATE);
        CommentText commentText = requestGroup.getCommentText();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}", "${type}", "${path-list-template}", "${link}"},
                new String[]{
                        requestGroup.getName(),
                        requestGroup.getType(),
                        reportPaths(requestGroup.getPath()),
                        "rg-" + buildId(requestGroup.getType()) + ".html"
                });
        return replace(template, commentText);
    }

    private String report(Document document, RequestGroup requestGroup) {
        String template = getTemplate(REQUEST_GROUP_TEMPLATE);
        CommentText commentText = requestGroup.getCommentText();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}", "${type}", "${path-list-template}",
                        "${request-list-template}", "${entry-list-template}"},
                new String[]{
                        requestGroup.getName(),
                        requestGroup.getType(),
                        reportPaths(requestGroup.getPath()),
                        reportRequests(requestGroup.getRequests()),
                        reportEntries(document, requestGroup.getEntryClassNames())
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
                        "${type.format}",
                        "${returnType.name}",
                        "${returnType.simpleName}",
                        "${returnType.type}",
                        "${returnType.type-id}",
                        "${returnType.format}"
                },
                new String[]{
                        type == null ? "" : defaultString(type.getName()),
                        type == null ? "" : defaultString(type.getSimpleName()),
                        type == null ? "" : defaultString(type.getType()),
                        type == null ? "" : buildId(type.getType()),
                        type == null ? "" : defaultString(type.getFormat()),
                        type == null ? "" : defaultString(type.getType()),
                        type == null ? "" : defaultString(type.getSimpleName()),
                        type == null ? "" : defaultString(type.getType()),
                        type == null ? "" : buildId(type.getType()),
                        type == null ? "" : defaultString(type.getFormat())
                });
    }

    private String reportRequests(List<Request> requests) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < requests.size(); i++) {
            result.append(report(i, requests.get(i)));

        }
        return result.toString();
    }

    private String report(int idx, Request request) {
        String template = getTemplate(REQUEST_TEMPLATE);
        CommentText commentText = request.getCommentText();
        TypeDesc returnType = request.getReturnType();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}", "${type-id}", "${path}",
                        "${method}",
                        "${returnCommentText}",
                        "${method-path-list-template}",
                        "${request-param-group-list-template}"
                },
                new String[]{
                        request.getName(),
                        "req-" + idx,
                        ArrayUtils.toString(request.getPath()),
                        ArrayUtils.toString(request.getMethod()),
                        request.getReturnCommentText(),
                        report(request.getMethod(), request.getPath()),
                        reportRequestParamGroups(request.getParams())
                });
        template = replace(template, commentText);
        return replace(template, returnType);
    }

    private String reportRequestParamGroups(List<RequestParam> requestParams) {
        String template = getTemplate(REQUEST_PARAM_GROUP_TEMPLATE);
        Map<RequestParamType, List<RequestParam>> map = requestParams.stream().collect(Collectors.groupingBy(RequestParam::getParamType));
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<RequestParamType, List<RequestParam>> entry : map.entrySet()) {
            sb.append(StringUtils.replaceEach(template,
                    new String[]{
                            "${paramType}",
                            "${request-param-list-template}"
                    },
                    new String[]{
                            entry.getKey().name(),
                            reportRequestParams(entry.getValue())
                    }));
        }
        return sb.toString();
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

    private String reportEntries(Document document, Set<String> entryClassNames) {
        if (entryClassNames == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        entryClassNames.stream().map(n -> document.getEntryMap().get(n)).filter(Objects::nonNull).forEach(entry -> {
            sb.append(report(entry));
        });
        return sb.toString();
    }

    private String report(Entry entry) {
        List<EntryField> fields = entry.getFields();
        Entry impl = entry.getImpl();
        if (impl != null) {
            fields = impl.getFields();
        }
        String template = getTemplate(ENTRY_TEMPLATE);
        CommentText commentText = entry.getCommentText();
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${simpleName}",
                        "${type}",
                        "${type-id}",
                        "${entry-field-list-template}",
                },
                new String[]{
                        entry.getSimpleName(),
                        entry.getType(),
                        buildId(entry.getType()),
                        reportEntryFields(fields)
                });
        return replace(template, commentText);
    }

    private String reportEntryFields(List<EntryField> entryFields) {
        StringBuilder sb = new StringBuilder();
        for (EntryField entryField : entryFields) {
            sb.append(report(entryField));
        }
        return sb.toString();
    }

    private String report(EntryField entryField) {
        String template = getTemplate(ENTRY_FIELD_TEMPLATE);
        template = StringUtils.replaceEach(template,
                new String[]{
                        "${name}",
                },
                new String[]{
                        entryField.getName(),
                });
        template = replace(template, entryField.getCommentText());
        return replace(template, entryField.getType());
    }
}
