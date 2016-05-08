package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.ConcurrentRequestProcessingEngineWrapper;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.EngineFactories;
import io.github.kostyaby.engine.EngineFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by kostya_by on 5/1/16.
 */
public class ConcurrentMultiThreadedBenchmarkingClient {
    private static void benchmarkMultiThreadedEngine(
            MongoDatabase database, ForkJoinPool executorService, List<DBRef> origins)
            throws IOException, ExecutionException, InterruptedException {

        EngineFactory engineFactory = new EngineFactories.MultiThreadedEngineFactory(database, executorService);
        try (Engine engine = new ConcurrentRequestProcessingEngineWrapper(executorService, engineFactory)) {
            System.err.println("Processing requests in multiple threads concurrently: "
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
            ForkJoinPool executorService = new ForkJoinPool();
            List<DBRef> origins = BenchmarkingClientUtils.getOriginsForBenchmarking(database);

            benchmarkMultiThreadedEngine(database, executorService, origins);
        }
    }
}
