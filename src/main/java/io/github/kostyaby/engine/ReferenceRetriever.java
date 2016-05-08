package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import org.bson.Document;

import java.util.List;

/**
 * Created by kostya_by on 4/17/16.
 */
interface ReferenceRetriever {
    List<DBRef> retrieveReferences(DBRef dbRef, int maxBranchingFactor);
    List<DBRef> retrieveReferences(Document document, int maxBranchingFactor);
}
