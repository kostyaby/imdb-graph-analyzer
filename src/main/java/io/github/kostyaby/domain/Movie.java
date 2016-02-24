package io.github.kostyaby.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.kostyaby.parser.ParserUtil.parseNumber;

public class Movie {
    private final String fullTitle;
    private final String shortTitle;
    private final int releaseYear;

    private final static Pattern MOVIE_PATTERN =
            Pattern.compile("(?<fullTitle>(?<shortTitle>[^\"].*) \\([0-9?]{4}(\\/[ILVX]+)?\\))( \\((TV|V|VG)\\))?( \\{\\{SUSPENDED\\}\\})? (?<releaseYear>[0-9?]{4})");

    public Movie(String fullTitle, String shortTitle, int releaseYear) {
        this.fullTitle = fullTitle;
        this.shortTitle = shortTitle;
        this.releaseYear = releaseYear;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public static Movie parseMovie(String line) {
        Matcher matcher = MOVIE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return null;
        }

        String fullName = matcher.group("fullTitle");
        String shortName = matcher.group("shortTitle");
        int releaseYear = parseNumber(matcher.group("releaseYear"));

        return new Movie(fullName, shortName, releaseYear);
    }
}
