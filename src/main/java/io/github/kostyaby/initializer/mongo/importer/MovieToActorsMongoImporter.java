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
public class MovieToActorsMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, ObjectId> actorMongoIds;
    private final Map<Integer, List<Integer>> moviesToActors;

    public MovieToActorsMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, ObjectId> actorMongoIds,
            Map<Integer, List<Integer>> moviesToActors) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.actorMongoIds = actorMongoIds;
        this.moviesToActors = moviesToActors;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME),
                moviesToActors.entrySet().stream().collect(Collectors.toList()),
                movieToActorEntry -> new Document("_id", movieMongoIds.get(movieToActorEntry.getKey())),
                movieToActorEntry ->
                        new Document(
                                "actors",
                                movieToActorEntry.getValue().stream()
                                        .map(actorMongoIds::get)
                                        .collect(Collectors.toList())));
    }
}
