package io.github.kostyaby.engine.models;

import com.mongodb.DBRef;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Actor extends Model {
    private final String name;
    private final String gender;

    private final List<DBRef> movies;

    public Actor(ObjectId id, String name, String gender, List<DBRef> movies) {
        super(id);

        this.name = name;
        this.gender = gender;
        this.movies = movies;
    }

    @Override
    public String getCollectionName() {
        return "actors";
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toPrettyString() {
        return String.format("actor; id: %s; name: %s; gender: %s", getId().toString(), name, gender);
    }
}
