package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by kostya_by on 4/16/16.
 */
public class EngineUtils {
    public static Document fetchDocument(MongoDatabase database, DBRef dbRef) {
        Objects.requireNonNull(database);
        Objects.requireNonNull(dbRef);

        return database.getCollection(dbRef.getCollectionName()).find(eq("_id", dbRef.getId())).first();
    }

    public static Document getDocument(Document document, String key) {
        Objects.requireNonNull(document);
        Objects.requireNonNull(key);

        return (Document) document.get(key);
    }

    public static List<String> getListOfStrings(Document document, String key) {
        Objects.requireNonNull(document);
        Objects.requireNonNull(key);

        return (List<String>) document.get(key);
    }

    public static List<DBRef> getListOfDBRefs(Document document, String key) {
        Objects.requireNonNull(document);
        Objects.requireNonNull(key);

        return (List<DBRef>) document.get(key);
    }
}
