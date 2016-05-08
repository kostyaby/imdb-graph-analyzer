package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.models.Model;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by kostya_by on 5/8/16.
 */
class BenchmarkingClientUtils {
    private static final int TOP_RECORDS_COUNTER = 50;

    private static Stream<DBRef> getTopRecords(
            MongoDatabase database, String collectionName, Comparator<Document> comparator) {
        return StreamSupport
                .stream(database.getCollection(collectionName).find().spliterator(), false)
                .sorted(comparator)
                .limit(TOP_RECORDS_COUNTER)
                .map(document -> new DBRef(collectionName, document.getObjectId("_id")));
    }

    private static List<DBRef> getDBRefs(Document document, String key) {
        List<DBRef> dbRefs = (List<DBRef>) document.get(key);

        if (dbRefs != null) {
            return dbRefs;
        } else {
            return new ArrayList<>();
        }
    }

    private static Comparator<Document> getComparatorOnKey(String key) {
        return (a, b) -> {
            if (getDBRefs(a, key).size() != getDBRefs(b, key).size()) {
                return getDBRefs(b, key).size() - getDBRefs(a, key).size();
            }

            return a.getObjectId("_id").compareTo(b.getObjectId("_id"));
        };
    }

    private static Stream<DBRef> getActorsForBenchmarking(MongoDatabase database) {
        return getTopRecords(database, "actors", getComparatorOnKey("movies"));
    }

    private static Stream<DBRef> getDirectorsForBenchmarking(MongoDatabase database) {
        return getTopRecords(database, "directors", getComparatorOnKey("movies"));
    }

    private static Stream<DBRef> getMoviesForBenchmarking(MongoDatabase database) {
        return getTopRecords(database, "movies", getComparatorOnKey("actors"));
    }

    static List<DBRef> getOriginsForBenchmarking(MongoDatabase database) {
        return Stream
                .concat(Stream.concat(getActorsForBenchmarking(database),
                        getDirectorsForBenchmarking(database)),
                        getMoviesForBenchmarking(database))
                .collect(Collectors.toList());
    }

    static long benchmarkEngine(Engine engine, List<DBRef> origins) throws ExecutionException, InterruptedException {
        long processingRequestsStart = System.currentTimeMillis();

        List<Future<List<Model>>> modelListFutures = new ArrayList<>();
        for (DBRef origin : origins) {
            modelListFutures.add(engine.processRequest(ClientUtils.newDefaultQuery(origin)));
        }

        int totalSize = 0;
        for (Future<List<Model>> modelListFuture : modelListFutures) {
            totalSize += modelListFuture.get().size();
        }

        System.err.println("Total size: " + totalSize);

        return System.currentTimeMillis() - processingRequestsStart;
    }
}
