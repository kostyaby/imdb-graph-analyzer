package io.github.kostyaby.initializer.mongo.identificator;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import io.github.kostyaby.initializer.domain.Movie;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieMongoIdentificator implements MongoIdentificator {
    private final MongoDatabase database;
    private final Map<Integer, Movie> movies;

    public MovieMongoIdentificator(MongoDatabase database, Map<Integer, Movie> movies) {
        this.database = database;
        this.movies = movies;
    }

    @Override
    public Map<Integer, ObjectId> identify() {
        Map<String, ObjectId> nameToMongoIdentity = new HashMap<>();

        Consumer<Document> movieCollectionConsumer =
                document -> nameToMongoIdentity.put(document.getString("title"), document.getObjectId("_id"));

        database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME).find().forEach(movieCollectionConsumer);

        Map<Integer, ObjectId> movieMongoIds = new HashMap<>();
        movies.entrySet().forEach(movieEntry ->
                movieMongoIds.put(
                        movieEntry.getKey(),
                        nameToMongoIdentity.get(movieEntry.getValue().getTitle())));

        return movieMongoIds;
    }
}
