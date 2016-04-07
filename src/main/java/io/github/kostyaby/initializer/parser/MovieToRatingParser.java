package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Rating;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToRatingParser implements Parser<Rating> {
    private final BufferedReader reader;

    public MovieToRatingParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, Rating> parse() {
        Map<Integer, Rating> ratings = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            int movieRank = (int) Math.round(10.0 * jsonObject.getDouble("rank"));
            int movieVotes = jsonObject.getInt("votes");

            ratings.put(movieId, new Rating(movieRank, movieVotes));
        });

        return ratings;
    }
}
