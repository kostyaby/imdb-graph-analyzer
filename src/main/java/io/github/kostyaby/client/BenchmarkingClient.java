package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.*;
import io.github.kostyaby.engine.models.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by kostya_by on 5/1/16.
 */
public class BenchmarkingClient {
    private static final int TOP_RECORDS_COUNTER = 100;

    public static Stream<DBRef> getTopRecords(MongoDatabase database, String collectionName) {
        return StreamSupport
                .stream(database.getCollection(collectionName).find().limit(TOP_RECORDS_COUNTER).spliterator(), false)
                .map(document -> new DBRef(collectionName, document.getObjectId("_id")));
    }

    public static Stream<DBRef> getActorsForBenchmarking(MongoDatabase database) {
        return getTopRecords(database, "actors");
    }

    public static Stream<DBRef> getDirectorsForBenchmarking(MongoDatabase database) {
        return getTopRecords(database, "directors");
    }

    public static Stream<DBRef> getMoviesForBenchmarking(MongoDatabase database) {
        return getTopRecords(database, "movies");
    }

    public static List<DBRef> getOriginsForBenchmarking(MongoDatabase database) {
        return Stream
                .concat(Stream.concat(getActorsForBenchmarking(database),
                        getDirectorsForBenchmarking(database)),
                        getMoviesForBenchmarking(database))
                .collect(Collectors.toList());
    }

    public static long benchmarkEngine(Engine engine, List<DBRef> origins)
            throws ExecutionException, InterruptedException {
        long processingRequestsStart = System.currentTimeMillis();

        List<Future<List<Model>>> modelListFutures = new ArrayList<>();
        for (DBRef origin : origins) {
            modelListFutures.add(engine.processRequest(Request.newBuilder()
                    .setOrigin(origin)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.ACTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.DIRECTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.MOVIES_REFERENCE_RETRIEVER)
                    .build()));
        }

        int totalSize = 0;
        for (Future<List<Model>> modelListFuture : modelListFutures) {
            totalSize += modelListFuture.get().size();
        }

        System.err.println("Total size: " + totalSize);

        return System.currentTimeMillis() - processingRequestsStart;
    }

    public static void benchmarkSingleThreadedEngine(MongoDatabase database, List<DBRef> origins)
            throws IOException, ExecutionException, InterruptedException {

        try (Engine engine = new SingleThreadedEngine(database)) {
            System.err.println("Processing requests in a single thread: "
                    + benchmarkEngine(engine, origins) + "ms");
        }
    }

    public static void benchmarkMultiThreadedEngine(MongoDatabase database, List<DBRef> origins)
            throws IOException, ExecutionException, InterruptedException {

        try (Engine engine = new MultiThreadedEngine(database)) {
            System.err.println("Processing requests in multiple threads: "
                    + benchmarkEngine(engine, origins) + "ms");
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        if (args.length != 1) {
            System.out.println(String.format("Invalid number of arguments: expected %d got %d", 1, args.length));
            System.exit(1);
            return;
        }

        MongoClientURI clientUri = new MongoClientURI(args[0]);

        try (MongoClient client = new MongoClient(clientUri)) {
            MongoDatabase database = client.getDatabase(clientUri.getDatabase());
            List<DBRef> origins = getOriginsForBenchmarking(database);

            benchmarkSingleThreadedEngine(database, origins);
            benchmarkMultiThreadedEngine(database, origins);
        }
    }
}
