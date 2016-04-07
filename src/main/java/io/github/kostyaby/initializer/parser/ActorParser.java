package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Actor;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by kostya_by on 4/7/16.
 */
public class ActorParser implements Parser<Actor> {
    private static final Function<String, String> GENDER_MAPPER = gender -> gender.equals("F") ? "female" : "male";

    private final BufferedReader reader;

    public ActorParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, Actor> parse() {
        Map<Integer, Actor> actors = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(
                jsonObject -> actors.put(
                        jsonObject.getInt("actorid"),
                        new Actor(
                                jsonObject.getString("name"),
                                GENDER_MAPPER.apply(jsonObject.getString("sex")))));

        return actors;
    }
}
