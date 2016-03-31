package io.github.kostyaby.parser;

import io.github.kostyaby.domain.Actor;
import io.github.kostyaby.domain.Director;
import io.github.kostyaby.domain.Movie;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ImdbDataParser {
    private final InputOutputData inputOutputData;

    ImdbDataParser(InputOutputData inputOutputData) {
        this.inputOutputData = inputOutputData;
    }

    List<JsonObjectWrapper> parseJsonArray(Reader reader) {
        return (List<JsonObjectWrapper>) ((JSONArray) JSONValue.parse(reader)).stream()
                .map(jsonObject -> new JsonObjectWrapper((JSONObject) jsonObject))
                .collect(Collectors.toList());
    }

    void parseMovies(Map<Integer, Movie> movies) {
        parseJsonArray(inputOutputData.getMoviesReader()).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            String movieTitle = jsonObject.getString("title");
            int year = jsonObject.getInt("year");

            Movie movie = new Movie(movieId);
            movie.setTitle(movieTitle);
            movie.setYear(year);

            movies.put(movieId, movie);
        });
    }

    void parseCountries(Map<Integer, Movie> movies) {
        parseJsonArray(inputOutputData.getCountriesReader()).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            String movieCountry = jsonObject.getString("country");

            movies.get(movieId).addCountry(movieCountry);
        });
    }

    void parseGenres(Map<Integer, Movie> movies) {
        parseJsonArray(inputOutputData.getGenresReader()).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            String movieGenre = jsonObject.getString("genre");

            movies.get(movieId).addGenre(movieGenre);
        });
    }

    void parseRatings(Map<Integer, Movie> movies) {
        parseJsonArray(inputOutputData.getRatingsReader()).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            int movieRank = (int) Math.round(10.0 * jsonObject.getDouble("rank"));
            int movieVotes = jsonObject.getInt("votes");

            movies.get(movieId).setRank(movieRank);
            movies.get(movieId).setVotes(movieVotes);
        });
    }

    void parseActors(Map<Integer, Actor> actors) {
        Function<String, String> genderMapper = gender -> gender.equals("F") ? "female" : "male";

        parseJsonArray(inputOutputData.getActorsReader()).forEach(jsonObject -> {
            int actorId = jsonObject.getInt("actorid");
            String actorName = jsonObject.getString("name");
            String actorGender = genderMapper.apply(jsonObject.getString("sex"));

            Actor actor = new Actor(actorId);
            actor.setName(actorName);
            actor.setGender(actorGender);

            actors.put(actorId, actor);
        });
    }

    void parseDirectors(Map<Integer, Director> directors) {
        parseJsonArray(inputOutputData.getDirectorsReader()).forEach(jsonObject -> {
            int directorId = jsonObject.getInt("directorid");
            String directorName = jsonObject.getString("name");

            Director director = new Director(directorId);
            director.setName(directorName);

            directors.put(directorId, director);
        });
    }

    void parseMoviesToActors(Map<Integer, Movie> movies, Map<Integer, Actor> actors) {
        parseJsonArray(inputOutputData.getMoviesToActorsReader()).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            int actorId = jsonObject.getInt("actorid");

            Movie movie = movies.get(movieId);
            Actor actor = actors.get(actorId);

            movie.addActor(actor);
            actor.addMovie(movie);
        });
    }

    void parseMoviesToDirectors(Map<Integer, Movie> movies, Map<Integer, Director> directors) {
        parseJsonArray(inputOutputData.getMoviesToDirectorsReader()).forEach(jsonObject -> {
            int movieId = jsonObject.getInt("movieid");
            int directorId = jsonObject.getInt("directorid");

            Movie movie = movies.get(movieId);
            Director director = directors.get(directorId);

            movie.addDirector(director);
            director.addMovie(movie);
        });
    }

    public void parse() throws IOException {
        Map<Integer, Movie> movies = new HashMap<>();
        Map<Integer, Actor> actors = new HashMap<>();
        Map<Integer, Director> directors = new HashMap<>();

        parseMovies(movies);
        parseCountries(movies);
        parseGenres(movies);
        parseRatings(movies);
        parseActors(actors);
        parseDirectors(directors);
        parseMoviesToActors(movies, actors);
        parseMoviesToDirectors(movies, directors);

        printMovies(movies);
        printActors(actors);
        printDirectors(directors);
    }

    void printMovies(Map<Integer, Movie> movies) throws IOException {
        long moviesWithoutCountries = movies.values().stream().filter(movie -> movie.getCountries().isEmpty()).count();
        long moviesWithoutGenres = movies.values().stream().filter(movie -> movie.getGenres().isEmpty()).count();
        long moviesWithoutRank = movies.values().stream().filter(movie -> movie.getVotes() == 0).count();
        long moviesWithoutDirector = movies.values().stream().filter(movie -> movie.getDirectors().isEmpty()).count();

//        for (Movie movie : movies.values()) {
//            inputOutputData.getLogWriter().write(movie.toPrettyString() + "\n\n");
//        }

        inputOutputData.getLogWriter().write("Movies without countries: " + moviesWithoutCountries + "\n");
        inputOutputData.getLogWriter().write("Movies without genres: " + moviesWithoutGenres + "\n");
        inputOutputData.getLogWriter().write("Movies without rank: " + moviesWithoutRank + "\n");
        inputOutputData.getLogWriter().write("Movies without directors: " + moviesWithoutDirector + "\n");
    }

    void printActors(Map<Integer, Actor> actors) throws IOException {
        long actorsWithoutMovies = actors.values().stream().filter(actor -> actor.getMovies().isEmpty()).count();

//        for (Actor actor : actors.values()) {
//            inputOutputData.getLogWriter().write(actor.toPrettyString() + "\n\n");
//        }

        inputOutputData.getLogWriter().write("Actors without movies: " + actorsWithoutMovies + "\n");
    }

    void printDirectors(Map<Integer, Director> directors) throws IOException {
        long directorsWithoutMovies = directors.values().stream().filter(director -> director.getMovies().isEmpty()).count();

//        for (Director director : directors.values()) {
//            inputOutputData.getLogWriter().write(director.toPrettyString() + "\n\n");
//        }

        inputOutputData.getLogWriter().write("Directors without movies: " + directorsWithoutMovies);
    }

    private static BufferedReader getReader(String fileName) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
    }

    private static BufferedWriter getWriter(String fileName) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 10) {
            System.out.println("Expected arguments: actors countries directors genres languages movies moviesToActors moviesToDirectors ratings log");
            System.exit(1);
            return;
        }

        InputOutputData inputOutputData = new InputOutputData(
                getReader(args[0]),
                getReader(args[1]),
                getReader(args[2]),
                getReader(args[3]),
                getReader(args[4]),
                getReader(args[5]),
                getReader(args[6]),
                getReader(args[7]),
                getReader(args[8]),
                getWriter(args[9]));

        try {
            new ImdbDataParser(inputOutputData).parse();
        } finally {
            inputOutputData.close();
        }
    }
}
