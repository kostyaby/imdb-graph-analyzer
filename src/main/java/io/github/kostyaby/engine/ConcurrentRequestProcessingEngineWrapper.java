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
public class ConcurrentRequestProcessingEngineWrapper implements Engine {
    private final int THREADS_COUNTER = Runtime.getRuntime().availableProcessors();

    private final EngineFactory engineFactory;
    private final ExecutorService executorService;

    public ConcurrentRequestProcessingEngineWrapper(ExecutorService executorService, EngineFactory engineFactory) {
        Objects.requireNonNull(executorService);
        Objects.requireNonNull(engineFactory);

        this.executorService = executorService;
        this.engineFactory = engineFactory;
    }

    @Override
    public Future<List<Model>> processRequest(final Request request) throws ExecutionException, InterruptedException {
        return executorService.submit(() -> engineFactory.newEngine().processRequest(request).get());
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}
