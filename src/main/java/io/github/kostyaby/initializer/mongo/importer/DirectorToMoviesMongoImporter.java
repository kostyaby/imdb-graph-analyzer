package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.DBRef;
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
public class DirectorToMoviesMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> directorMongoIds;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, List<Integer>> directorsToMovies;

    public DirectorToMoviesMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> directorMongoIds,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, List<Integer>> directorsToMovies) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.directorMongoIds = directorMongoIds;
        this.directorsToMovies = directorsToMovies;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.DIRECTORS_COLLECTION_NAME),
                directorsToMovies.entrySet().stream().collect(Collectors.toList()),
                directorToMovieEntry -> new Document("_id", directorMongoIds.get(directorToMovieEntry.getKey())),
                directorToMovieEntry ->
                        new Document(
                                "movies",
                                directorToMovieEntry.getValue().stream()
                                        .map(id -> new DBRef(
                                                MongoConstants.MOVIES_COLLECTION_NAME,
                                                movieMongoIds.get(id)))
                                        .collect(Collectors.toList())));
    }
}
