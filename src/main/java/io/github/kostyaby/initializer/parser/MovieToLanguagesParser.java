package io.github.kostyaby.initializer.parser;

import io.github.kostyaby.initializer.domain.Language;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public class MovieToLanguagesParser implements Parser<List<Language>> {
    private final BufferedReader reader;

    public MovieToLanguagesParser(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Map<Integer, List<Language>> parse() {
        Map<Integer, List<Language>> languages = new HashMap<>();

        ParserUtils.parseJsonArray(reader).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            String languageName = jsonObject.getString("language");

            ParserUtils.putToMultimap(languages, movieId, new Language(languageName));
        });

        return languages;
    }
}
