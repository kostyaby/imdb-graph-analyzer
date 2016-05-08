package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.ClientUtils;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.ReferenceRetriever;
import io.github.kostyaby.engine.Request;
import io.github.kostyaby.engine.models.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by kostya_by on 5/8/16.
 */
public class BenchmarkingClientUtils {
    private static final int TOP_RECORDS_COUNTER = 100;

    public static Stream<DBRef> getTopRecords(MongoDatabase database, String collectionName) {
        return StreamSupport
                .stream(database.getCollection(collectionName).find().spliterator(), false)
                .limit(TOP_RECORDS_COUNTER)
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
            modelListFutures.add(engine.processRequest(ClientUtils.newDefaultQuery(origin)));
        }

        for (Future<List<Model>> modelListFuture : modelListFutures) {
            modelListFuture.get();
        }

        return System.currentTimeMillis() - processingRequestsStart;
    }
}
