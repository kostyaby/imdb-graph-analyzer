package io.github.kostyaby.engine.models;

import com.mongodb.DBRef;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Movie extends Model {
    private final String title;
    private final int year;

    private final List<String> countries;
    private final List<String> genres;

    private final Rating rating;

    private final List<String> languages;
    private final List<DBRef> actors;

    private Movie(
            ObjectId id,
            String title,
            int year,
            List<String> countries,
            List<String> genres,
            Rating rating,
            List<String> languages,
            List<DBRef> actors) {
        super(id);

        this.title = title;
        this.year = year;
        this.countries = countries;
        this.genres = genres;
        this.rating = rating;
        this.languages = languages;
        this.actors = actors;
    }

    public static class Rating {
        private final int rank;
        private final int votes;

        private Rating(int rank, int votes) {
            this.rank = rank;
            this.votes = votes;
        }

        public static Rating newRating(Document document) {
            if (document != null) {
                return new Rating(
                        document.getInteger("rank"),
                        document.getInteger("votes"));
            } else {
                return null;
            }
        }

        public int getRank() {
            return rank;
        }

        public int getVotes() {
            return votes;
        }
    }

    public static Movie newMovie(Document document) {
        return new Movie(
                document.getObjectId("_id"),
                document.getString("title"),
                document.getInteger("year"),
                (List<String>) document.getOrDefault("countries", new ArrayList<>()),
                (List<String>) document.getOrDefault("genres", new ArrayList<>()),
                Rating.newRating((Document) document.get("rating")),
                (List<String>) document.getOrDefault("languages", new ArrayList<>()),
                ((List<ObjectId>) document.getOrDefault("actors", new ArrayList<>())).stream()
                        .map(objectId -> new DBRef("actors", objectId))
                        .collect(Collectors.toList()));
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
    public String getCollectionName() {
        return "movies";
    }

    @Override
    public String toPrettyString() {
        return String.format("movie; id: %s; title: %s, year: %d", getId().toString(), title, year);
    }
}
