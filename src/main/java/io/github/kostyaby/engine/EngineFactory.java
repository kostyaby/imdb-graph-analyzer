package io.github.kostyaby.engine;

import com.mongodb.client.MongoDatabase;

import java.util.Objects;

/**
 * Created by kostya_by on 5/8/16.
 */
public abstract class EngineFactory {
    final MongoDatabase database;

    EngineFactory(MongoDatabase database) {
        Objects.requireNonNull(database);

        this.database = database;
    }

    abstract public Engine newEngine();
}
