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

        ParserUtils.parseJsonArray(reader).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            String countryName = jsonObject.getString("country");

            ParserUtils.putToMultimap(countries, movieId, new Country(countryName));
        });

        return countries;
    }
}
