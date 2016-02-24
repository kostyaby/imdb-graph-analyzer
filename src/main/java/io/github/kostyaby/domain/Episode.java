package io.github.kostyaby.domain;

import io.github.kostyaby.parser.ParserUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.kostyaby.parser.ParserUtil.parseNumber;

public class Episode {
    private final String fullTitle;
    private final String shortTitle;
    private final int releaseYear;
    private final int seasonId;
    private final int episodeId;
    private final TvSeries tvSeries;

    private final static Pattern EPISODE_PATTERN =
            Pattern.compile("(?<tvSeriesFullTitle>\"(.+)\" \\([0-9?]{4}(/[ILVX]+)?\\)) \\{(?<fullTitle>.*)\\} (?<releaseYear>[0-9?]{4})");

    private final static Pattern EPISODE_NUMBER =
            Pattern.compile("\\(#(?<seasonId>\\d+).(?<episodeId>\\d+)\\)$");

    public Episode(String fullTitle, String shortTitle, int releaseYear, int seasonId, int episodeId, TvSeries tvSeries) {
        this.fullTitle = fullTitle;
        this.shortTitle = shortTitle;
        this.releaseYear = releaseYear;
        this.seasonId = seasonId;
        this.episodeId = episodeId;
        this.tvSeries = tvSeries;
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

    public int getSeasonId() {
        return seasonId;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public TvSeries getTvSeries() {
        return tvSeries;
    }

    public static Episode parseEpisode(String line) {
        Matcher matcher = EPISODE_PATTERN.matcher(line);
        if (!matcher.matches()) {
            return null;
        }

        String tvSeriesFullTitle = matcher.group("tvSeriesFullTitle");
        String fullTitle = matcher.group("fullTitle");
        String shortTitle = fullTitle;
        int releaseYear = parseNumber(matcher.group("releaseYear"));

        Matcher matcher1 = EPISODE_NUMBER.matcher(fullTitle);
        int seasonId;
        int episodeId;
        if (matcher1.find()) {
            shortTitle = shortTitle.replaceAll(EPISODE_NUMBER.toString(), "").trim();
            seasonId = parseNumber(matcher1.group("seasonId"));
            episodeId = parseNumber(matcher1.group("episodeId"));
        } else {
            seasonId = ParserUtil.DEFAULT_NUMBER;
            episodeId = ParserUtil.DEFAULT_NUMBER;
        }

        return new Episode(
                fullTitle,
                shortTitle,
                releaseYear,
                seasonId,
                episodeId,
                new TvSeries(
                        tvSeriesFullTitle,
                        null,
                        ParserUtil.DEFAULT_NUMBER,
                        ParserUtil.DEFAULT_NUMBER));
    }
}
