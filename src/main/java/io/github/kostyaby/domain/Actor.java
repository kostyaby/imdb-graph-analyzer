package io.github.kostyaby.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 3/31/16.
 */
public class Actor {
    private final int id;

    private String name;
    private String gender;

    private List<Movie> movies = new ArrayList<>();

    public Actor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public String toPrettyString() {
        return String.format(
                "Id: %d\nName: %s\nGender: %s\nMovies: %s",
                id,
                name,
                gender,
                String.join("; ", movies.stream().map(Movie::getTitle).collect(Collectors.toList())));
    }
}
