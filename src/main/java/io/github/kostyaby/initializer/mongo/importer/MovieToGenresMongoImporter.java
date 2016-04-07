package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Genre;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToGenresMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, List<Genre>> genres;

    public MovieToGenresMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, List<Genre>> genres) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.genres = genres;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME),
                genres.entrySet().stream().collect(Collectors.toList()),
                genreEntry -> new Document("_id", movieMongoIds.get(genreEntry.getKey())),
                genreEntry ->
                        new Document(
                                "genres",
                                genreEntry.getValue().stream()
                                        .map(Genre::getName)
                                        .collect(Collectors.toList())));
    }
}
