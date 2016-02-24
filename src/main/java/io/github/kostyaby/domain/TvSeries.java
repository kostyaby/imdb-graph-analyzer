package io.github.kostyaby.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.kostyaby.parser.ParserUtil.parseNumber;

public class TvSeries {
    private final String fullTitle;
    private final String shortTitle;
    private final int releaseYear;
    private final int endingYear;
    private final List<Episode> episodes;

    private final static Pattern TV_SERIES_PATTERN =
            Pattern.compile("(?<fullTitle>\"(?<shortTitle>.+)\" \\([0-9?]{4}(/[ILVX]+)?\\))( \\{\\{SUSPENDED\\}\\})? (?<releaseYear>[0-9?]{4})(-(?<endingYear>[0-9?]{4}))?");

    public TvSeries(String fullName, String shortName, int releaseYear, int endingYear) {
        this.fullTitle = fullName;
        this.shortTitle = shortName;
        this.releaseYear = releaseYear;
        this.endingYear = endingYear;
        episodes = new ArrayList<>();
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

    public int getEndingYear() {
        return endingYear;
    }

    public static TvSeries parseTvSeries(String line) {
        Matcher matcher = TV_SERIES_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return null;
        }

        String fullTitle = matcher.group("fullTitle");
        String shortTitle = matcher.group("shortTitle");
        int releaseYear = parseNumber(matcher.group("releaseYear"));
        int endingYear;
        if (matcher.group("endingYear") != null) {
            endingYear = parseNumber(matcher.group("endingYear"));
        } else {
            endingYear = releaseYear;
        }

        return new TvSeries(fullTitle, shortTitle, releaseYear, endingYear);
    }
}
