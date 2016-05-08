package io.github.kostyaby.engine;

import com.mongodb.client.MongoDatabase;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by kostya_by on 5/8/16.
 */
public class EngineFactories {
    public static class SingleThreadedEngineFactory extends EngineFactory {
        public SingleThreadedEngineFactory(MongoDatabase database) {
            super(database);
        }

        @Override
        public Engine newEngine() {
            return new SingleThreadedEngine(database);
        }
    }

    public static class MultiThreadedEngineFactory extends EngineFactory {
        private final ForkJoinPool executorService;

        public MultiThreadedEngineFactory(MongoDatabase database, ForkJoinPool executorService) {
            super(database);
            Objects.requireNonNull(executorService);

            this.executorService = executorService;
        }

        @Override
        public Engine newEngine() {
            return new MultiThreadedEngine(database, executorService);
        }
    }
}
