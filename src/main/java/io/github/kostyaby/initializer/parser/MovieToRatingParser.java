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

        ParserUtils.parseJsonArray(reader).forEach(
                jsonObject -> ratings.put(
                        jsonObject.getInt("movieid"),
                        new Rating(
                                getMovieRank(jsonObject.getDouble("rank")),
                                jsonObject.getInt("votes"))));

        return ratings;
    }

    private int getMovieRank(double doubleMovieRank) {
        return (int) Math.round(doubleMovieRank);
    }
}
