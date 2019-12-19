package ren.crux.rainbow.core;

import lombok.Getter;
import lombok.Setter;
import ren.crux.rainbow.core.docs.model.Document;

import java.util.Optional;

@Getter
@Setter
public class DocumentReader {

    private RequestGroupProvider requestGroupProvider;
    private CLassDescProvider cLassDescProvider;

    public Optional<Document> read(Context context) {
        return Optional.empty();
    }


}
