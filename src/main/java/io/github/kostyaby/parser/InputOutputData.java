package io.github.kostyaby.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by kostya_by on 3/31/16.
 */
public class InputOutputData {
    private final BufferedReader actorsReader;
    private final BufferedReader countriesReader;
    private final BufferedReader directorsReader;
    private final BufferedReader genresReader;
    private final BufferedReader languagesReader;
    private final BufferedReader moviesReader;
    private final BufferedReader moviesToActorsReader;
    private final BufferedReader moviesToDirectorsReader;
    private final BufferedReader ratingsReader;

    private final BufferedWriter logWriter;

    public InputOutputData(
            BufferedReader actorsReader,
            BufferedReader countriesReader,
            BufferedReader directorsReader,
            BufferedReader genresReader,
            BufferedReader languagesReader,
            BufferedReader moviesReader,
            BufferedReader moviesToActorsReader,
            BufferedReader moviesToDirectorsReader,
            BufferedReader ratingsReader,
            BufferedWriter logWriter) {
        this.actorsReader = actorsReader;
        this.countriesReader = countriesReader;
        this.directorsReader = directorsReader;
        this.genresReader = genresReader;
        this.languagesReader = languagesReader;
        this.moviesReader = moviesReader;
        this.moviesToActorsReader = moviesToActorsReader;
        this.moviesToDirectorsReader = moviesToDirectorsReader;
        this.ratingsReader = ratingsReader;
        this.logWriter = logWriter;
    }

    public BufferedReader getActorsReader() {
        return actorsReader;
    }

    public BufferedReader getCountriesReader() {
        return countriesReader;
    }

    public BufferedReader getDirectorsReader() {
        return directorsReader;
    }

    public BufferedReader getGenresReader() {
        return genresReader;
    }

    public BufferedReader getLanguagesReader() {
        return languagesReader;
    }

    public BufferedReader getMoviesReader() {
        return moviesReader;
    }

    public BufferedReader getMoviesToActorsReader() {
        return moviesToActorsReader;
    }

    public BufferedReader getMoviesToDirectorsReader() {
        return moviesToDirectorsReader;
    }

    public BufferedReader getRatingsReader() {
        return ratingsReader;
    }

    public BufferedWriter getLogWriter() {
        return logWriter;
    }

    public void close() throws IOException {
        actorsReader.close();
        countriesReader.close();
        directorsReader.close();
        genresReader.close();
        languagesReader.close();
        moviesReader.close();
        moviesToActorsReader.close();
        moviesToDirectorsReader.close();
        ratingsReader.close();
        logWriter.close();
    }
}
