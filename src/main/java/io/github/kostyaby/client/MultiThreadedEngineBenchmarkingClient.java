package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.MultiThreadedEngine;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by kostya_by on 5/1/16.
 */
public class MultiThreadedEngineBenchmarkingClient {
    private static void benchmarkMultiThreadedEngine(MongoDatabase database, List<DBRef> origins)
            throws IOException, ExecutionException, InterruptedException {

        try (Engine engine = new MultiThreadedEngine(database)) {
            System.err.println("Processing requests in multiple threads: "
                    + BenchmarkingClientUtils.benchmarkEngine(engine, origins) + "ms");
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
            List<DBRef> origins = BenchmarkingClientUtils.getOriginsForBenchmarking(database);

            benchmarkMultiThreadedEngine(database, origins);
        }
    }
}
