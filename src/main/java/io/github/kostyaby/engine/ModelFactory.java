package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import io.github.kostyaby.engine.models.Actor;
import io.github.kostyaby.engine.models.Director;
import io.github.kostyaby.engine.models.Model;
import io.github.kostyaby.engine.models.Movie;
import org.bson.Document;

import java.util.Objects;

/**
 * Created by kostya_by on 4/16/16.
 */
class ModelFactory {
    static Model newModel(DBRef dbRef, Document document) {
        Objects.requireNonNull(dbRef);
        Objects.requireNonNull(document);

        switch (dbRef.getCollectionName()) {
            case "actors":
                return newActor(document);
            case "directors":
                return newDirector(document);
            case "movies":
                return newMovie(document);
            default:
                return null;
        }
    }

    private static Actor newActor(Document document) {
        return new Actor(
                document.getObjectId("_id"),
                document.getString("name"),
                document.getString("gender"),
                EngineUtils.getDBRefs(document, "movies"));
    }

    private static Director newDirector(Document document) {
        return new Director(
                document.getObjectId("_id"),
                document.getString("name"),
                EngineUtils.getDBRefs(document, "movies"));
    }

    private static Movie newMovie(Document document) {
        return new Movie(
                document.getObjectId("_id"),
                document.getString("title"),
                document.getInteger("year"),
                EngineUtils.getStrings(document, "countries"),
                EngineUtils.getStrings(document, "genres"),
                newMovieRating(EngineUtils.getDocument(document, "rating")),
                EngineUtils.getStrings(document, "languages"),
                EngineUtils.getDBRefs(document, "actors"));
    }

    private static Movie.Rating newMovieRating(Document document) {
        if (document != null) {
            return new Movie.Rating(
                    document.getInteger("rank"),
                    document.getInteger("votes"));
        } else {
            return null;
        }
    }
}
