package io.github.kostyaby.initializer.mongo.identificator;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import io.github.kostyaby.initializer.domain.Director;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by kostya_by on 4/7/16.
 */
public class DirectorMongoIdentificator implements MongoIdentificator {
    private final MongoDatabase database;
    private final Map<Integer, Director> directors;

    public DirectorMongoIdentificator(MongoDatabase database, Map<Integer, Director> directors) {
        this.database = database;
        this.directors = directors;
    }

    @Override
    public Map<Integer, ObjectId> identify() {
        Map<String, ObjectId> nameToMongoIdentity = new HashMap<>();

        Consumer<Document> directorCollectionConsumer =
                document -> nameToMongoIdentity.put(document.getString("name"), document.getObjectId("_id"));

        database.getCollection(MongoConstants.DIRECTORS_COLLECTION_NAME).find().forEach(directorCollectionConsumer);

        Map<Integer, ObjectId> directorMongoIds = new HashMap<>();
        directors.entrySet().forEach(directorEntry ->
                directorMongoIds.put(
                        directorEntry.getKey(),
                        nameToMongoIdentity.get(directorEntry.getValue().getName())));

        return directorMongoIds;
    }
}
