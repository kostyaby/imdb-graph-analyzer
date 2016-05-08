package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kostya_by on 5/8/16.
 */
class CommonReferenceRetriever implements ReferenceRetriever {
    private final MongoDatabase database;
    private final List<ReferenceRetriever> referenceRetrievers = new ArrayList<>();

    CommonReferenceRetriever(MongoDatabase database) {
        Objects.requireNonNull(database);

        this.database = database;
        this.referenceRetrievers.add(new ActorsReferenceRetriever(database));
        this.referenceRetrievers.add(new DirectorsReferenceRetriever(database));
        this.referenceRetrievers.add(new MoviesReferenceRetriever(database));
    }

    @Override
    public List<DBRef> retrieveReferences(DBRef dbRef, int maxBranchingFactor) {
        Objects.requireNonNull(dbRef);

        return retrieveReferences(EngineUtils.fetchDocument(database, dbRef), maxBranchingFactor);
    }

    @Override
    public List<DBRef> retrieveReferences(Document document, int maxBranchingFactor) {
        Objects.requireNonNull(document);

        List<DBRef> result = new ArrayList<>();
        for (ReferenceRetriever referenceRetriever : referenceRetrievers) {
            result.addAll(
                    referenceRetriever.retrieveReferences(
                            document,
                            maxBranchingFactor / referenceRetrievers.size() + 1));
        }

        return result.subList(0, Math.min(maxBranchingFactor, result.size()));
    }
}
