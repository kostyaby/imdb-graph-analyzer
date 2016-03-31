package io.github.kostyaby.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Movie {
    private final int id;

    private String title;
    private int year;

    private int rank;
    private int votes;

    private List<String> countries = new ArrayList<>();
    private List<String> genres = new ArrayList<>();

    private List<Director> directors = new ArrayList<>();
    private List<Actor> actors = new ArrayList<>();

    public Movie(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void addCountry(String country) {
        countries.add(country);
    }

    public List<String> getGenres() {
        return genres;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void addDirector(Director director) {
        directors.add(director);
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        actors.add(actor);
    }

    public String toPrettyString() {
        return String.format(
                "Id: %d\nTitle: %s\nYear: %d\nRank: %d\nVotes: %d\nCountries: %s\nGenres: %s\nDirectors: %s\nActors: %s",
                id,
                title,
                year,
                rank,
                votes,
                String.join(", ", countries),
                String.join(", ", genres),
                String.join("; ", directors.stream().map(Director::getName).collect(Collectors.toList())),
                String.join("; ", actors.stream().map(Actor::getName).collect(Collectors.toList())));
    }
}
