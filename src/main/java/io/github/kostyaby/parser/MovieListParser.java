package io.github.kostyaby.parser;

import io.github.kostyaby.domain.Episode;
import io.github.kostyaby.domain.Movie;
import io.github.kostyaby.domain.TvSeries;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MovieListParser {
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public MovieListParser(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public void parse() throws IOException {
        List<Movie> movies = new ArrayList<>();
        List<TvSeries> tvSeries = new ArrayList<>();
        List<Episode> episodes = new ArrayList<>();
        int unparsedCounter = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\\s+", " ");

            Movie anotherMovie = Movie.parseMovie(line);
            if (anotherMovie != null) {
                movies.add(anotherMovie);
            } else {
                TvSeries anotherTvSeries = TvSeries.parseTvSeries(line);
                if (anotherTvSeries != null) {
                    tvSeries.add(anotherTvSeries);
                } else {
                    Episode anotherEpisode = Episode.parseEpisode(line);
                    if (anotherEpisode != null) {
                        episodes.add(anotherEpisode);
                    } else {
                        unparsedCounter += 1;
                    }
                }
            }
        }

//        writer.write("Movies: " + movies.size() + "\n");
//        writer.write("TV Shows: " + tvSeries.size() + "\n");
//        writer.write("Episode: " + episodes.size() + "\n");
//        writer.write("Unparsed: " + unparsedCounter + "\n");
//        writer.write("----------------------------\n");
//
//        for (Movie movie : movies) {
//            writer.write(movie.getFullTitle() + "|" + movie.getShortTitle() + "|" + movie.getReleaseYear() + "\n");
//        }
//
//        for (TvSeries tvSeries1 : tvSeries) {
//            writer.write(tvSeries1.getFullTitle() + "|" + tvSeries1.getShortTitle() + "|" + tvSeries1.getReleaseYear() + "|" + tvSeries1.getEndingYear() + "\n");
//        }
//
//        for (Episode episode: episodes) {
//            writer.write(episode.getTvSeries().getFullTitle()
//                    + "|" + episode.getFullTitle()
//                    + "|" + episode.getShortTitle()
//                    + "|" + episode.getReleaseYear()
//                    + "|" + episode.getSeasonId()
//                    + "|" + episode.getEpisodeId()
//                    + "\n");
//        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Expected arguments: movies-list.in movies-list.out");
            System.exit(1);
            return;
        }

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "ISO-8859-1"));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"));

            new MovieListParser(reader, writer).parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Something went completely wrong: IOException was thrown inside finally-block!");
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Something went completely wrong: IOException was thrown inside finally-block!");
                }
            }
        }
    }
}
