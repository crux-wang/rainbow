package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 请求组
 *
 * @author wangzhihui
 */
@Data
public class RequestGroup {
    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 注释
     */
    private CommentText commentText;
    /**
     * 路径
     */
    private String[] path;
    /**
     * 请求列表
     */
    private List<Request> requests;
    /**
     * 此请求组所用到的实体类型
     */
    private Set<String> entryClassNames;

    public void addRequest(@NonNull Request request) {
        if (requests == null) {
            requests = new LinkedList<>();
        }
        requests.add(request);
    }

    public void addEntryClassName(String className) {
        if (StringUtils.isNotBlank(className)) {
            className = StringUtils.substringBefore(className, "[");
            className = StringUtils.substringBefore(className, "<");
            if (StringUtils.equalsAny(className, "void", "int", "long", "float", "double", "byte", "boolean", "char", "short", "T", "E", "K", "V", "?")) {
                return;
            }
            if (StringUtils.startsWithAny(className, "java.lang.", "java.util.")) {
                return;
            }
            if (entryClassNames == null) {
                entryClassNames = new HashSet<>();
            }
            entryClassNames.add(className);
        }
    }

    public void addEntryClassName(TypeDesc typeDesc) {
        if (typeDesc != null) {
            addEntryClassName(typeDesc.getType());
            if (typeDesc.getActualParamTypes() != null) {
                for (TypeDesc actualParamType : typeDesc.getActualParamTypes()) {
                    addEntryClassName(actualParamType);
                }
            }
        }
    }

}
