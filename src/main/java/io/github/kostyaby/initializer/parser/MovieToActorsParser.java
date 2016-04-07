package io.github.kostyaby.initializer.parser;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToActorsParser implements Parser<List<Integer>> {
    private final BufferedReader reader;

    public MovieToActorsParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, List<Integer>> parse() {
        Map<Integer, List<Integer>> moviesToActors = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            int actorId = jsonObject.getInt("actorid");

            ParserUtils.putToMultimap(moviesToActors, movieId, actorId);
        });

        return moviesToActors;
    }
}
