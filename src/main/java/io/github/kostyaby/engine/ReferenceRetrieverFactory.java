package io.github.kostyaby.engine;

import com.mongodb.client.MongoDatabase;

import java.util.Objects;

/**
 * Created by kostya_by on 4/17/16.
 */
public class ReferenceRetrieverFactory {
    public static ReferenceRetriever newReferenceRetriever(
            MongoDatabase database, ReferenceRetriever.Type referenceRetrieverType) {
        Objects.requireNonNull(database);
        Objects.requireNonNull(referenceRetrieverType);

        switch (referenceRetrieverType) {
            case ACTORS_REFERENCE_RETRIEVER:
                return new ActorsReferenceRetriever(database);
            case DIRECTORS_REFERENCE_RETRIEVER:
                return new DirectorsReferenceRetriever(database);
            case MOVIES_REFERENCE_RETRIEVER:
                return new MoviesReferenceRetriever(database);
            default:
                return null;
        }
    }
}
