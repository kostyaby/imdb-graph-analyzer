package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Country;

import java.io.BufferedReader;
import java.util.*;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToCountriesParser implements Parser<List<Country>> {
    private final BufferedReader reader;

    public MovieToCountriesParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, List<Country>> parse() {
        Map<Integer, List<Country>> countries = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(
                jsonObject -> ParserUtils.putToMultimap(
                    countries,
                    jsonObject.getInt("movieid"),
                    new Country(jsonObject.getString("country"))));

        return countries;
    }
}
