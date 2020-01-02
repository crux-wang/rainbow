package ren.crux.rainbow.core.report;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import ren.crux.rainbow.core.model.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * markdown 格式
 *
 * @author wangzhihui
 */
public class MarkdownReport implements Reporter<Map<String, File>> {

    public static final MarkdownReport INSTANCE = new MarkdownReport();

    private boolean ignoredRequestAttr = true;

    public MarkdownReport ignoredRequestAttr(boolean ignoredRequestAttr) {
        this.ignoredRequestAttr = ignoredRequestAttr;
        return this;
    }

    private final String dirPath;

    public MarkdownReport(String dirPath) {
        this.dirPath = StringUtils.appendIfMissing(dirPath, "/");
    }

    public MarkdownReport() {
        this("/tmp/");
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

    /**
     * 汇报
     *
     * @param document 文档
     * @return 汇报结果
     */
    @Override
    public Optional<Map<String, File>> report(Document document) {
        Map<String, File> map = new HashMap<>();
        for (RequestGroup requestGroup : document.getRequestGroups()) {
            StringBuilder sb = new StringBuilder();
            if (requestGroup.getCommentText() == null) {
                sb.append("# ").append(requestGroup.getName());
            } else {
                sb.append("# ").append(requestGroup.getCommentText().firstLine()).append("\n\n")
                        .append("> ").append(StringUtils.defaultString(requestGroup.getCommentText().content(), " "));
            }
            sb.append("\n\n");
            if (requestGroup.getPath() == null) {
                sb.append("`/`");
            } else {
                for (String path : requestGroup.getPath()) {
                    sb.append("`").append(path).append("` ");
                }
            }
            sb.append("\n\n");
            List<Request> requests = requestGroup.getRequests();
            if (requests != null) {
                int i = 1;
                for (Request request : requests) {
                    sb.append("# ").append(i++).append(". ");
                    if (request.getCommentText() == null) {
                        sb.append(request.getName());
                    } else {
                        sb.append(request.getCommentText().firstLine());
                    }
                    sb.append("\n\n");
                    RequestMethod[] methods = request.getMethod();
                    if (methods != null) {
                        for (RequestMethod method : methods) {
                            sb.append("**").append(method.name()).append("**  ");
                        }
                    }
                    if (request.getPath() == null) {
                        sb.append("``");
                    } else {
                        for (String path : request.getPath()) {
                            sb.append("`").append(path).append("` ");
                        }
                    }
                    sb.append("\n\n");
                    List<RequestParam> params = request.getParams();
                    if (CollectionUtils.isEmpty(params)) {
                        sb.append("No Args\n\n");
                    } else {
                        Map<RequestParamType, List<RequestParam>> group = params.stream().collect(Collectors.groupingBy(RequestParam::getParamType));
                        for (Map.Entry<RequestParamType, List<RequestParam>> entry : group.entrySet()) {
                            if (entry.getKey() == RequestParamType.request_body) {
                                continue;
                            }
                            if (ignoredRequestAttr && entry.getKey() == RequestParamType.request_attribute) {
                                continue;
                            }
                            List<RequestParam> pms = entry.getValue();
                            sb.append("**").append(StringUtils.replace(entry.getKey().name(), "_", " ").toUpperCase()).append("**\n\n");
                            sb.append("|  name | type | required | default | comment text | other |\n| ------------ | ------------ | ------------ | ------------ | ------------ | ------------ |\n");
                            for (RequestParam pm : pms) {
                                sb.append("| ").append(pm.getName())
                                        .append(" | ").append(formatType(pm.getType()))
                                        .append(" | ").append(pm.isRequired() ? "Y" : "N")
                                        .append(" | ").append(StringUtils.defaultString(pm.getDefaultValue()))
                                        .append(" | ").append(pm.getCommentText() == null ? "-" : pm.getCommentText().inline())
                                        .append(" | ").append(formatAnnotations(pm.getAnnotations()))
                                        .append(" |\n");
                            }
                            sb.append("\n");
                        }
                        List<RequestParam> body = group.get(RequestParamType.request_body);
                        if (CollectionUtils.isNotEmpty(body)) {
                            RequestParam requestBody = body.get(0);
                            sb.append("**REQUEST BODY**\n\n")
                                    .append(requestBody.getType().getFormat()).append("\n\n");
                        }
                    }
                    sb.append("**RESPONSE**\n\n");
                    String returnType = formatType(request.getReturnType());
                    if (StringUtils.equals("void", returnType)) {
                        sb.append("None");
                    } else {
                        sb.append(returnType);
                    }
                    String returnCommentText = StringUtils.defaultString(request.getReturnCommentText());
                    if (StringUtils.isNotBlank(returnCommentText)) {
                        sb.append("  //  ").append(returnCommentText);
                    }
                    ;
                    sb.append("\n\n");
                }
            }
            Set<String> entryClassNames = requestGroup.getEntryClassNames();
            if (CollectionUtils.isNotEmpty(entryClassNames)) {
                sb.append("# 实体列表\n\n");
                int j = 1;
                for (String entryClassName : entryClassNames) {
                    Entry entry = document.getEntryMap().get(entryClassName);
                    if (entry != null) {
                        sb.append("## ").append(j++).append(". ").append(entry.getSimpleName()).append("\n\n");
                        if (entry.getImpl() != null) {
                            entry = entry.getImpl();
                        }
                        List<EntryField> fields = entry.getFields();
                        if (CollectionUtils.isNotEmpty(fields)) {
                            sb.append("|  name | type | comment text | other |\n| ------------ | ------------ | ------------ | ------------ |\n");
                            for (EntryField field : fields) {
                                sb.append("| ").append(field.getName())
                                        .append(" | ").append(formatType(field.getType()))
                                        .append(" | ").append(field.getCommentText() == null ? "-" : field.getCommentText().inline())
                                        .append(" | ").append(formatAnnotations(field.getAnnotations()))
                                        .append(" |\n");
                            }
                        }
                    }
                    sb.append("\n");
                }
            }
            String fileName = buildId(requestGroup.getType()) + ".md";
            File file = writeStringToFile(fileName, sb.toString());
            map.put(fileName, file);
        }
        if (map.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(map);
    }

    private String formatAnnotations(List<Annotation> annotations) {
        if (CollectionUtils.isEmpty(annotations)) {
            return "-";
        }
        StringBuilder str = new StringBuilder();
        for (Annotation annotation : annotations) {
            str.append("@").append(annotation.getName());
            if (annotation.getAttribute() != null) {
                Map<String, Object> attribute = new HashMap<>(annotation.getAttribute());
                if (StringUtils.startsWithAny(annotation.getType(), "javax.validation.constraints.", "org.hibernate.validator.constraints.")) {
                    attribute.remove("message");
                    attribute.remove("groups");
                    attribute.remove("payload");
                    attribute.remove("flags");
                }
                if (!attribute.isEmpty()) {
                    if (attribute.size() == 1 && attribute.containsKey("value")) {
                        Object value = attribute.get("value");
                        str.append("(").append(value).append(")");
                    } else {
                        str.append("(").append(attribute).append(")");
                    }
                }
            }
            str.append(" ; ");
        }
        return str.toString();
    }

    private String formatType(TypeDesc type) {
        String name = type.getSimpleName();
        if (ArrayUtils.isNotEmpty(type.getActualParamTypes())) {
            return name + " < " + Arrays.stream(type.getActualParamTypes()).map(this::formatType).collect(Collectors.joining(", ")) + " > ";
        }
        return name;
    }
}
