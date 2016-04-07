package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Movie;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieParser implements Parser<Movie> {
    private final BufferedReader reader;

    public MovieParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, Movie> parse() {
        Map<Integer, Movie> movies = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(jsonObject ->
            movies.put(
                    jsonObject.getInt("movieid"),
                    new Movie(
                            jsonObject.getString("title"),
                            jsonObject.getInt("year"))));

        return movies;
    }
}
