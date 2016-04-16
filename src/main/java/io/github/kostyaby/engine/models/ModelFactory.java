package io.github.kostyaby.engine.models;

import com.mongodb.DBRef;
import org.bson.Document;

/**
 * Created by kostya_by on 4/16/16.
 */
public class ModelFactory {
    public static Model newModel(DBRef dbRef, Document document) {
        if (dbRef.getCollectionName().equals("movies")) {
            return Movie.newMovie(document);
        } else {
            return null;
        }
    }
}
