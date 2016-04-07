package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Actor;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;

import java.util.Collection;

/**
 * Created by kostya_by on 4/7/16.
 */
public class ActorMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Collection<Actor> actors;

    public ActorMongoImporter(MongoDatabase database, Collection<Actor> actors) {
        this.database = database;
        this.actors = actors;
    }

    @Override
    public void importData() {
        database.createCollection(MongoConstants.ACTORS_COLLECTION_NAME);

        MongoImporterUtils.insertElements(
                database.getCollection(MongoConstants.ACTORS_COLLECTION_NAME), actors, actor -> {
                    Document document = new Document();

                    document.append("name", actor.getName());
                    document.append("gender", actor.getGender());

                    return document;
                });
    }
}
