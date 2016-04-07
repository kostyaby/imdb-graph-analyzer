package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class ActorToMoviesMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> actorMongoIds;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, List<Integer>> actorsToMovies;

    public ActorToMoviesMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> actorMongoIds,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, List<Integer>> actorsToMovies) {
        this.database = database;
        this.actorMongoIds = actorMongoIds;
        this.movieMongoIds = movieMongoIds;
        this.actorsToMovies = actorsToMovies;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.ACTORS_COLLECTION_NAME),
                actorsToMovies.entrySet().stream().collect(Collectors.toList()),
                actorToMovieEntry -> new Document("_id", actorMongoIds.get(actorToMovieEntry.getKey())),
                actorToMovieEntry ->
                        new Document(
                                "movies",
                                actorToMovieEntry.getValue().stream()
                                        .map(movieMongoIds::get)
                                        .collect(Collectors.toList())));
    }
}
