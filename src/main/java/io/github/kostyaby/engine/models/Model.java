package io.github.kostyaby.engine.models;

import com.mongodb.DBRef;
import org.bson.types.ObjectId;

import java.util.Objects;

/**
 * Created by kostya_by on 4/16/16.
 */
public abstract class Model {
    private final ObjectId id;

    public Model(ObjectId id) {
        Objects.requireNonNull(id);

        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public abstract String toPrettyString();
}
