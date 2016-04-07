package io.github.kostyaby.initializer.mongo.identificator;

import org.bson.types.ObjectId;

import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public interface MongoIdentificator {
    Map<Integer, ObjectId> identify();
}
