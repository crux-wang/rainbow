package ren.crux.rainbow.hub.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import ren.crux.raonbow.common.model.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DocumentService
 *
 * @author wangzhihui
 **/
@Service
public class DocumentService {

    private List<Document> documents = new ArrayList<>();

    public Optional<Document> get(String id) {
        return documents.isEmpty() ? Optional.empty() : Optional.of(documents.get(documents.size() - 1));
    }

    public List<Document> history(String id) {
        return documents;
    }

    public void add(String id, @NonNull Document document) {
        documents.add(document);
    }

}
