package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kostya_by on 5/8/16.
 */
public class CommonReferenceRetriever implements ReferenceRetriever {
    private final List<ReferenceRetriever> referenceRetrievers = new ArrayList<>();

    public CommonReferenceRetriever(MongoDatabase database) {
        Objects.requireNonNull(database);

        referenceRetrievers.add(new ActorsReferenceRetriever(database));
        referenceRetrievers.add(new DirectorsReferenceRetriever(database));
        referenceRetrievers.add(new MoviesReferenceRetriever(database));
    }

    @Override
    public List<DBRef> retrieveReferences(DBRef dbRef, int maxBranchingFactor) {
        Objects.requireNonNull(dbRef);

        List<DBRef> result = new ArrayList<>();
        for (ReferenceRetriever referenceRetriever : referenceRetrievers) {
            result.addAll(
                    referenceRetriever.retrieveReferences(dbRef, maxBranchingFactor / referenceRetrievers.size() + 1));
        }

        return result.subList(0, Math.min(maxBranchingFactor, result.size()));
    }
}
