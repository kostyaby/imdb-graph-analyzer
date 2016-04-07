package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Genre;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToGenresParser implements Parser<List<Genre>> {
    private final BufferedReader reader;

    public MovieToGenresParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, List<Genre>> parse() {
        Map<Integer, List<Genre>> genres = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            String genreName = jsonObject.getString("genre");

            ParserUtils.putToMultimap(genres, movieId, new Genre(genreName));
        });

        return genres;
    }
}
