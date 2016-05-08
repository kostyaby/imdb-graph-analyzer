package io.github.kostyaby.engine;

import com.mongodb.DBRef;

import java.util.List;

/**
 * Created by kostya_by on 4/17/16.
 */
interface ReferenceRetriever {
    List<DBRef> retrieveReferences(DBRef model, int maxBranchingFactor);
}
