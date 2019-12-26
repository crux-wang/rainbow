package ren.crux.rainbow.core;

import org.apache.commons.lang3.ArrayUtils;
import ren.crux.rainbow.core.dict.TypeDict;
import ren.crux.rainbow.core.model.*;
import ren.crux.rainbow.core.report.Reporter;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            for (EntryMethod method : entry.getMethods()) {
                TypeDesc returnType = method.getReturnType();
                returnType.setFormat(typeDict.translate(returnType));
            }
        }
        return this;
    }

    public DocumentStream ignored(IgnoredType... ignoredTypes) {
        if (ArrayUtils.isEmpty(ignoredTypes)) {
            return this;
        }
        Set<IgnoredType> types = Arrays.stream(ignoredTypes).collect(Collectors.toSet());
        List<RequestGroup> requestGroups = document.getRequestGroups();
        for (RequestGroup requestGroup : requestGroups) {
            if (types.contains(IgnoredType.tags)) {
                ignoredTags(requestGroup.getCommentText());
            }
            for (Request request : requestGroup.getRequests()) {
                if (types.contains(IgnoredType.tags)) {
                    ignoredTags(request.getCommentText());
                }
                for (RequestParam param : request.getParams()) {
                    if (types.contains(IgnoredType.annotation)) {
                        param.setAnnotations(null);
                    } else if (types.contains(IgnoredType.annotation_attrs)) {
                        ignoredAnnotationAttrs(param.getAnnotations());
                    }
                    if (types.contains(IgnoredType.tags)) {
                        ignoredTags(param.getCommentText());
                    }
                }
            }
        }
        for (Entry entry : document.getEntryMap().values()) {
            if (types.contains(IgnoredType.tags)) {
                ignoredTags(entry.getCommentText());
            }
            if (types.contains(IgnoredType.annotation)) {
                entry.setAnnotations(null);
            } else if (types.contains(IgnoredType.annotation_attrs)) {
                ignoredAnnotationAttrs(entry.getAnnotations());
            }
            for (EntryField field : entry.getFields()) {
                if (types.contains(IgnoredType.annotation)) {
                    field.setAnnotations(null);
                } else if (types.contains(IgnoredType.annotation_attrs)) {
                    ignoredAnnotationAttrs(field.getAnnotations());
                }
                if (types.contains(IgnoredType.tags)) {
                    ignoredTags(field.getCommentText());
                }
            }
            for (EntryMethod method : entry.getMethods()) {
                if (types.contains(IgnoredType.annotation)) {
                    method.setAnnotations(null);
                } else if (types.contains(IgnoredType.annotation_attrs)) {
                    ignoredAnnotationAttrs(method.getAnnotations());
                }
                if (types.contains(IgnoredType.tags)) {
                    ignoredTags(method.getCommentText());
                }
            }
        }
        return this;
    }

    private void ignoredAnnotationAttrs(List<Annotation> annotations) {
        if (annotations != null) {
            annotations.forEach(a -> a.setAttribute(null));
        }
    }

    private void ignoredTags(CommentText commentText) {
        if (commentText != null) {
            commentText.setTags(null);
        }
    }

    public static enum IgnoredType {
        annotation_attrs,
        annotation,
        tags
    }
}
