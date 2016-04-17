package io.github.kostyaby.engine.models;

import com.mongodb.DBRef;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Movie extends Model {
    private final String title;
    private final Integer year;

    private final List<String> countries;
    private final List<String> genres;

    private final Rating rating;

    private final List<String> languages;
    private final List<DBRef> actors;

    public Movie(
            ObjectId id,
            String title,
            Integer year,
            List<String> countries,
            List<String> genres,
            Rating rating,
            List<String> languages,
            List<DBRef> actors) {
        super(id);

        Objects.requireNonNull(title);
        Objects.requireNonNull(year);

        this.title = title;
        this.year = year;
        this.countries = countries;
        this.genres = genres;
        this.rating = rating;
        this.languages = languages;
        this.actors = actors;
    }

    public static class Rating {
        private final Integer rank;
        private final Integer votes;

        public Rating(Integer rank, Integer votes) {
            Objects.requireNonNull(rank);
            Objects.requireNonNull(votes);

            this.rank = rank;
            this.votes = votes;
        }

        public int getRank() {
            return rank;
        }

        public int getVotes() {
            return votes;
        }
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public List<String> getCountries() {
        return countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Rating getRating() {
        return rating;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<DBRef> getActors() {
        return actors;
    }

    @Override
    public String toPrettyString() {
        return String.format("movie; id: %s; title: %s; year: %d", getId().toString(), title, year);
    }
}
