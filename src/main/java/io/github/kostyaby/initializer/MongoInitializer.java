package io.github.kostyaby.initializer;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.initializer.mongo.identificator.ActorMongoIdentificator;
import io.github.kostyaby.initializer.mongo.identificator.DirectorMongoIdentificator;
import io.github.kostyaby.initializer.mongo.identificator.MovieMongoIdentificator;
import io.github.kostyaby.initializer.domain.*;
import io.github.kostyaby.initializer.mongo.importer.*;
import io.github.kostyaby.initializer.parser.*;
import org.bson.types.ObjectId;

import java.io.*;

import java.util.*;

public class MongoInitializer {
    private final DataReaders dataReaders;
    private final MongoDatabase database;

    public MongoInitializer(DataReaders dataReaders, MongoDatabase database) {
        this.dataReaders = dataReaders;
        this.database = database;
    }

    public void initialize() throws IOException {
        Map<Integer, Movie> movies = new MovieParser(dataReaders.getMoviesReader()).parse();
        Map<Integer, List<Country>> countries = new MovieToCountriesParser(dataReaders.getCountriesReader()).parse();
        Map<Integer, List<Genre>> genres = new MovieToGenresParser(dataReaders.getGenresReader()).parse();
        Map<Integer, List<Language>> languages = new MovieToLanguagesParser(dataReaders.getLanguagesReader()).parse();
        Map<Integer, Rating> ratings = new MovieToRatingParser(dataReaders.getRatingsReader()).parse();
        Map<Integer, Actor> actors = new ActorParser(dataReaders.getActorsReader()).parse();
        Map<Integer, Director> directors = new DirectorParser(dataReaders.getDirectorsReader()).parse();

        Map<Integer, List<Integer>> moviesToActors =
                new MovieToActorsParser(dataReaders.getMoviesToActorsReader()).parse();
        Map<Integer, List<Integer>> actorsToMovies = ParserUtils.reverseMultimap(moviesToActors);
        Map<Integer, List<Integer>> moviesToDirectors =
                new MovieToDirectorsParser(dataReaders.getMoviesToDirectorsReader()).parse();
        Map<Integer, List<Integer>> directorsToMovies = ParserUtils.reverseMultimap(moviesToDirectors);

        new MovieMongoImporter(database, movies.values()).importData();
        new ActorMongoImporter(database, actors.values()).importData();
        new DirectorMongoImporter(database, directors.values()).importData();

        Map<Integer, ObjectId> movieMongoIds = new MovieMongoIdentificator(database, movies).identify();
        Map<Integer, ObjectId> actorMongoIds = new ActorMongoIdentificator(database, actors).identify();
        Map<Integer, ObjectId> directorMongoIds = new DirectorMongoIdentificator(database, directors).identify();

        new MovieToCountriesMongoImporter(database, movieMongoIds, countries).importData();
        new MovieToGenresMongoImporter(database, movieMongoIds, genres).importData();
        new MovieToRatingMongoImporter(database, movieMongoIds, ratings).importData();
        new MovieToLanguagesMongoImporter(database, movieMongoIds, languages).importData();

        new MovieToActorsMongoImporter(database, movieMongoIds, actorMongoIds, moviesToActors).importData();
        new ActorToMoviesMongoImporter(database, actorMongoIds, movieMongoIds, actorsToMovies).importData();

        new MovieToDirectorsMongoImporter(database, movieMongoIds, directorMongoIds, moviesToDirectors).importData();
        new DirectorToMoviesMongoImporter(database, directorMongoIds, movieMongoIds, directorsToMovies).importData();
    }

    private static BufferedReader getReader(String fileName) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 10) {
            System.out.println(String.format("Invalid number of arguments: expected %d got %d", 10, args.length));
            System.exit(1);
            return;
        }

        DataReaders dataReaders = new DataReaders(
                getReader(args[0]),
                getReader(args[1]),
                getReader(args[2]),
                getReader(args[3]),
                getReader(args[4]),
                getReader(args[5]),
                getReader(args[6]),
                getReader(args[7]),
                getReader(args[8]));

        MongoClientURI clientUri = new MongoClientURI(args[9]);

        MongoClient client = new MongoClient(clientUri);
        MongoDatabase database = client.getDatabase(clientUri.getDatabase());
        database.drop();

        try {
            new MongoInitializer(dataReaders, database).initialize();
        } finally {
            dataReaders.close();
            client.close();
        }
    }
}
