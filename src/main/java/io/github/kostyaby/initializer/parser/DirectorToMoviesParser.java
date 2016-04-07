package io.github.kostyaby.initializer.parser;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class DirectorToMoviesParser implements Parser<List<Integer>> {
    private final BufferedReader reader;

    public DirectorToMoviesParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, List<Integer>> parse() {
        Map<Integer, List<Integer>> directorsToMovies = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            int directorId = jsonObject.getInt("directorid");

            ParserUtils.putToMultimap(directorsToMovies, directorId, movieId);
        });

        return directorsToMovies;
    }
}
