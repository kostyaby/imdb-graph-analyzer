package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Director;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;

import java.util.Collection;

/**
 * Created by kostya_by on 4/7/16.
 */
public class DirectorMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Collection<Director> directors;

    public DirectorMongoImporter(MongoDatabase database, Collection<Director> directors) {
        this.database = database;
        this.directors = directors;
    }

    @Override
    public void importData() {
        database.createCollection(MongoConstants.DIRECTORS_COLLECTION_NAME);

        MongoImporterUtils.insertElements(
                database.getCollection(MongoConstants.DIRECTORS_COLLECTION_NAME), directors,
                        director -> new Document("name", director.getName()));
    }
}
