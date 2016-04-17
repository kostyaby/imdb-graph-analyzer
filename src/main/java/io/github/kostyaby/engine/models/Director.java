package io.github.kostyaby.engine.models;

import com.mongodb.DBRef;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

/**
 * Created by kostya_by on 4/17/16.
 */
public class Director extends Model {
    private final String name;
    private final List<DBRef> movies;

    public Director(ObjectId id, String name, List<DBRef> movies) {
        super(id);

        Objects.requireNonNull(name);

        this.name = name;
        this.movies = movies;
    }

    public String getName() {
        return name;
    }

    public List<DBRef> getMovies() {
        return movies;
    }

    @Override
    public String toPrettyString() {
        return String.format("director; id: %s; name: %s", getId().toString(), name);
    }
}
