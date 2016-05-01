package io.github.kostyaby.engine;

import com.mongodb.DBRef;

import java.util.List;

/**
 * Created by kostya_by on 4/17/16.
 */
public interface ReferenceRetriever {
    List<DBRef> retrieveReferences(DBRef model, int maxBranchingFactor);

    /**
     * Created by kostya_by on 4/17/16.
     */
    enum Type {
        ACTORS_REFERENCE_RETRIEVER,
        DIRECTORS_REFERENCE_RETRIEVER,
        MOVIES_REFERENCE_RETRIEVER
    }
}
