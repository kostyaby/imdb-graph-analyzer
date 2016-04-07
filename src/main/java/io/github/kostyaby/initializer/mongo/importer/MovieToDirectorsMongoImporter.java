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
public class MovieToDirectorsMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, ObjectId> directorMongoIds;
    private final Map<Integer, List<Integer>> moviesToDirectors;

    public MovieToDirectorsMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, ObjectId> directorMongoIds,
            Map<Integer, List<Integer>> moviesToDirectors) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.directorMongoIds = directorMongoIds;
        this.moviesToDirectors = moviesToDirectors;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME),
                moviesToDirectors.entrySet().stream().collect(Collectors.toList()),
                movieToDirectorEntry -> new Document("_id", movieMongoIds.get(movieToDirectorEntry.getKey())),
                movieToDirectorEntry ->
                        new Document(
                                "directors",
                                movieToDirectorEntry.getValue().stream()
                                        .map(directorMongoIds::get)
                                        .collect(Collectors.toList())));
    }
}
