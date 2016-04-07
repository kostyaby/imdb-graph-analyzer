package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Country;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToCountriesMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, List<Country>> countries;

    public MovieToCountriesMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, List<Country>> countries) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.countries = countries;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME),
                countries.entrySet().stream().collect(Collectors.toList()),
                countryEntry -> new Document("_id", movieMongoIds.get(countryEntry.getKey())),
                countryEntry ->
                        new Document(
                                "countries",
                                countryEntry.getValue().stream()
                                        .map(Country::getName)
                                        .collect(Collectors.toList())));
    }
}
