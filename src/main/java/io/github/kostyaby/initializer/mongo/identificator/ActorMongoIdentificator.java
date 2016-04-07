package io.github.kostyaby.initializer.mongo.identificator;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import io.github.kostyaby.initializer.domain.Actor;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by kostya_by on 4/7/16.
 */
public class ActorMongoIdentificator implements MongoIdentificator {
    private final MongoDatabase database;
    private final Map<Integer, Actor> actors;

    public ActorMongoIdentificator(MongoDatabase database, Map<Integer, Actor> actors) {
        this.database = database;
        this.actors = actors;
    }

    @Override
    public Map<Integer, ObjectId> identify() {
        Map<String, ObjectId> nameToMongoIdentity = new HashMap<>();

        Consumer<Document> actorCollectionConsumer =
                document -> nameToMongoIdentity.put(document.getString("name"), document.getObjectId("_id"));

        database.getCollection(MongoConstants.ACTORS_COLLECTION_NAME).find().forEach(actorCollectionConsumer);

        Map<Integer, ObjectId> actorMongoIds = new HashMap<>();
        actors.entrySet().forEach(actorEntry ->
                actorMongoIds.put(
                        actorEntry.getKey(),
                        nameToMongoIdentity.get(actorEntry.getValue().getName())));

        return actorMongoIds;
    }
}
