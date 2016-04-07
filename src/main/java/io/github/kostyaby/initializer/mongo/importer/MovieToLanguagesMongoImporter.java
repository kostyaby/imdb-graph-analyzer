package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Language;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToLanguagesMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, List<Language>> languages;

    public MovieToLanguagesMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, List<Language>> languages) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.languages = languages;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME),
                languages.entrySet().stream().collect(Collectors.toList()),
                languageEntry -> new Document("_id", movieMongoIds.get(languageEntry.getKey())),
                languageEntry ->
                        new Document(
                                "languages",
                                languageEntry.getValue().stream()
                                        .map(Language::getName)
                                        .collect(Collectors.toList())));
    }
}
