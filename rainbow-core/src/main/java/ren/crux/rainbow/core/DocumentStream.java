package ren.crux.rainbow.core;

import ren.crux.rainbow.core.dict.TypeDict;
import ren.crux.rainbow.core.model.*;
import ren.crux.rainbow.core.report.Reporter;

import java.util.List;
import java.util.Optional;

public class DocumentStream {

    private Document document;

    public DocumentStream(Document document) {
        this.document = document;
    }

    public Optional<Document> get() {
        return Optional.ofNullable(document);
    }

    public <T> Optional<T> report(Reporter<T> reporter) {
        return get().flatMap(reporter::report);
    }

    public DocumentStream translate(TypeDict typeDict) {
        List<RequestGroup> requestGroups = document.getRequestGroups();
        for (RequestGroup requestGroup : requestGroups) {
            for (Request request : requestGroup.getRequests()) {
                TypeDesc returnType = request.getReturnType();
                returnType.setFormat(typeDict.translate(returnType));
                for (RequestParam param : request.getParams()) {
                    TypeDesc paramType = param.getType();
                    paramType.setFormat(typeDict.translate(paramType));
                }
            }
        }
        for (Entry entry : document.getEntryMap().values()) {
            for (EntryField field : entry.getFields()) {
                TypeDesc fieldType = field.getType();
                fieldType.setFormat(typeDict.translate(fieldType));
            }
        }
        return this;
    }

}
