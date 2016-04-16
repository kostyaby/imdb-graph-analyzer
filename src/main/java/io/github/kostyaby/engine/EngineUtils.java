package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by kostya_by on 4/16/16.
 */
public class EngineUtils {
    public static Document fetchDocument(MongoDatabase database, DBRef dbRef) {
        return database.getCollection(dbRef.getCollectionName()).find(eq("_id", dbRef.getId())).first();
    }
}
