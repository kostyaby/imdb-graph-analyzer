package io.github.kostyaby.engine;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.models.Model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by kostya_by on 5/1/16.
 */
public class MultiThreadedEngine implements Engine {
    private final int THREAD_COUNTER = 12;

    private final MongoDatabase database;

    private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNTER);

    public MultiThreadedEngine(MongoDatabase database) {
        Objects.requireNonNull(database);

        this.database = database;
    }

    @Override
    public Future<List<Model>> processRequest(final Request request) throws ExecutionException, InterruptedException {
        return executorService.submit(() -> new SingleThreadedEngine(database).processRequest(request).get());
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}
