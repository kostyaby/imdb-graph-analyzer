package io.github.kostyaby.engine;

import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.models.Model;
import io.github.kostyaby.engine.models.ModelFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kostya_by on 4/16/16.
 */
public class SingleThreadEngine implements Engine {
    private final MongoDatabase database;

    public SingleThreadEngine(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public List<Model> processRequest(Request request) {
        Model model = ModelFactory.newModel(request.getOrigin(), EngineUtils.fetchDocument(database, request.getOrigin()));

        return Arrays.asList(model);
    }
}
