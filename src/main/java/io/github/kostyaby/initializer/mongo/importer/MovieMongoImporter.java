package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Movie;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;

import java.util.Collection;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Collection<Movie> movies;

    public MovieMongoImporter(MongoDatabase database, Collection<Movie> movies) {
        this.database = database;
        this.movies = movies;
    }

    @Override
    public void importData() {
        database.createCollection(MongoConstants.MOVIES_COLLECTION_NAME);

        MongoImporterUtils.insertElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME), movies,
                        movie -> new Document()
                                .append("title", movie.getTitle())
                                .append("year", movie.getYear()));
    }
}
