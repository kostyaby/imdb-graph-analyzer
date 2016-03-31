package io.github.kostyaby.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 3/31/16.
 */
public class Director {
    private final int id;

    private String name;

    private List<Movie> movies = new ArrayList<>();

    public Director(int id) {
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

    public List<Movie> getMovies() {
        return movies;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public String toPrettyString() {
        return String.format(
                "Id: %d\nName: %s\nMovies: %s",
                id,
                name,
                String.join("; ", movies.stream()
                        .map(Movie::getTitle)
                        .collect(Collectors.toList())));
    }
}
