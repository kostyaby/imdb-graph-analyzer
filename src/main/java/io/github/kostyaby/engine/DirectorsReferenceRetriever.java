package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kostya_by on 4/17/16.
 */
class DirectorsReferenceRetriever implements ReferenceRetriever {
    private final MongoDatabase database;

    DirectorsReferenceRetriever(MongoDatabase database) {
        Objects.requireNonNull(database);

        this.database = database;
    }

    @Override
    public List<DBRef> retrieveReferences(DBRef dbRef, int maxBranchingFactor) {
        Objects.requireNonNull(dbRef);

        return retrieveReferences(EngineUtils.fetchDocument(database, dbRef), maxBranchingFactor);
    }

    @Override
    public List<DBRef> retrieveReferences(Document document, int maxBranchingFactor) {
        Objects.requireNonNull(document);

        List<DBRef> result = EngineUtils.getDBRefs(document, "directors");
        if (result != null) {
            return result.subList(0, Math.min(maxBranchingFactor, result.size()));
        } else {
            return new ArrayList<>();
        }
    }
}
