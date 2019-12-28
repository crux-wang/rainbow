package ren.crux.rainbow.editor.model;

import lombok.Data;
import ren.crux.rainbow.core.model.Document;
import ren.crux.rainbow.core.model.Entry;
import ren.crux.rainbow.core.model.Request;
import ren.crux.rainbow.core.model.RequestGroup;

import java.util.Map;

/**
 * DocsListUtils
 *
 * @author wangzhihui
 **/
@Data
public class DocsListUtils {

    public static Document form(Document document) {
        if (document == null) {
            return null;
        }
        Document dump = new Document();
        dump.getProperties().putAll(document.getProperties());
        if (document.getRequestGroups() != null) {
            for (RequestGroup requestGroup : document.getRequestGroups()) {
                RequestGroup rg = new RequestGroup();
                rg.setCommentText(requestGroup.getCommentText());
                rg.setName(requestGroup.getName());
                rg.setPath(requestGroup.getPath());
                dump.addRequestGroup(rg);
            }
        }
        if (document.getEntryMap() != null) {
            for (Map.Entry<String, Entry> entry : document.getEntryMap().entrySet()) {
                Entry value = entry.getValue();
                Entry e = new Entry();
                e.setName(value.getName());
                e.setSimpleName(value.getSimpleName());
                e.setCommentText(value.getCommentText());
                e.setEnumType(value.isEnumType());
                e.setType(value.getType());
                dump.addEntry(e);
            }
        }
        return dump;
    }

    public static RequestGroup form(RequestGroup requestGroup) {
        if (requestGroup == null) {
            return null;
        }
        RequestGroup dump = new RequestGroup();
        dump.setPath(requestGroup.getPath());
        dump.setName(requestGroup.getName());
        dump.setCommentText(requestGroup.getCommentText());
        if (requestGroup.getRequests() != null) {
            for (Request request : requestGroup.getRequests()) {
                Request r = new Request();
                r.setName(request.getName());
                r.setCommentText(request.getCommentText());
                r.setMethod(request.getMethod());
                r.setPath(request.getPath());
                dump.addRequest(r);
            }
        }
        return dump;
    }
}
