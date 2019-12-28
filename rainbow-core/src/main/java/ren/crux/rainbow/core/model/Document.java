package ren.crux.rainbow.core.model;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 文档
 *
 * @author wangzhihui
 */
@Data
public class Document {

    /**
     * 其他属性
     */
    private final Map<String, Object> properties = new HashMap<>();
    /**
     * 请求组
     */
    private List<RequestGroup> requestGroups = new LinkedList<>();
    /**
     * 实体映射
     */
    private Map<String, Entry> entryMap = new HashMap<>();

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public void addRequestGroup(RequestGroup requestGroup) {
        requestGroups.add(requestGroup);
    }

    public void addEntry(Entry entry) {
        entryMap.put(entry.getType(), entry);
    }

}
