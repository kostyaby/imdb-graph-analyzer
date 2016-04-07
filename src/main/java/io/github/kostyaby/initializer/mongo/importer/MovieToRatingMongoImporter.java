package io.github.kostyaby.initializer.mongo.importer;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.domain.Rating;
import io.github.kostyaby.initializer.mongo.MongoConstants;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToRatingMongoImporter implements MongoImporter {
    private final MongoDatabase database;
    private final Map<Integer, ObjectId> movieMongoIds;
    private final Map<Integer, Rating> ratings;

    public MovieToRatingMongoImporter(
            MongoDatabase database,
            Map<Integer, ObjectId> movieMongoIds,
            Map<Integer, Rating> ratings) {
        this.database = database;
        this.movieMongoIds = movieMongoIds;
        this.ratings = ratings;
    }

    @Override
    public void importData() {
        MongoImporterUtils.updateElements(
                database.getCollection(MongoConstants.MOVIES_COLLECTION_NAME),
                ratings.entrySet().stream().collect(Collectors.toList()),
                ratingEntry -> new Document("_id", movieMongoIds.get(ratingEntry.getKey())),
                ratingEntry ->
                        new Document(
                                "rating",
                                new Document()
                                        .append("rank", ratingEntry.getValue().getRank())
                                        .append("votes", ratingEntry.getValue().getVotes())));
    }
}
