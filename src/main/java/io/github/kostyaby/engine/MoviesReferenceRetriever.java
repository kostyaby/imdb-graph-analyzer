package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kostya_by on 4/17/16.
 */
class MoviesReferenceRetriever implements ReferenceRetriever {
    private final MongoDatabase database;

    MoviesReferenceRetriever(MongoDatabase database) {
        Objects.requireNonNull(database);

        this.database = database;
    }

    @Override
    public List<DBRef> retrieveReferences(DBRef dbRef, int maxBranchingFactor) {
        Objects.requireNonNull(dbRef);

        List<DBRef> result = EngineUtils.getDBRefs(EngineUtils.fetchDocument(database, dbRef), "movies");
        if (result != null) {
            return result.subList(0, Math.min(maxBranchingFactor, result.size()));
        } else {
            return new ArrayList<>();
        }
    }
}
