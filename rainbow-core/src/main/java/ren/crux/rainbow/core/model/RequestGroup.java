package ren.crux.rainbow.core.model;

import lombok.Data;
import lombok.NonNull;
import ren.crux.rainbow.core.utils.EntryUtils;

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
        if (entryClassNames == null) {
            entryClassNames = new HashSet<>();
        }
        EntryUtils.addEntryClassName(entryClassNames, className);
    }

    public void addEntryClassName(TypeDesc typeDesc) {
        if (entryClassNames == null) {
            entryClassNames = new HashSet<>();
        }
        EntryUtils.addEntryClassName(entryClassNames, typeDesc);
    }

}
