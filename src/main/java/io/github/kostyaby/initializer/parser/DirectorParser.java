package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Director;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class DirectorParser implements Parser<Director> {
    private final BufferedReader reader;

    public DirectorParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, Director> parse() {
        Map<Integer, Director> directors = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(
                jsonObject -> directors.put(
                        jsonObject.getInt("directorid"),
                        new Director(jsonObject.getString("name"))));

        return directors;
    }
}
