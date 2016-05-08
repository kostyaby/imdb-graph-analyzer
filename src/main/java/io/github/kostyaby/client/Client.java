package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.Request;
import io.github.kostyaby.engine.SingleThreadedEngine;
import io.github.kostyaby.engine.models.Model;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Client {
    static DBRef getPulpFiction(MongoDatabase database) {
        return new DBRef(
                "movies",
                database.getCollection("movies").find(eq("title", "Pulp Fiction (1994)")).first().getObjectId("_id"));
    }

    static DBRef getBradPitt(MongoDatabase database) {
        return new DBRef(
                "actors",
                database.getCollection("actors").find(eq("name", "Pitt, Brad")).first().getObjectId("_id"));
    }

    static DBRef getQuentinTarantino(MongoDatabase database) {
        return new DBRef(
                "directors",
                database.getCollection("directors").find(eq("name", "Tarantino, Quentin")).first().getObjectId("_id"));
    }

    static void processRequest(Engine engine, Request request) throws ExecutionException, InterruptedException {
        System.err.println("Origin: " + request.getOrigin());
        List<Model> response = engine.processRequest(request).get();

        for (Model model : response) {
            System.err.println(model.toPrettyString());
        }

        System.err.println("Count: " + response.size());
        System.err.println("****************************");
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != 1) {
            System.out.println(String.format("Invalid number of arguments: expected %d got %d", 1, args.length));
            System.exit(1);
            return;
        }

        MongoClientURI clientUri = new MongoClientURI(args[0]);

        MongoClient client = new MongoClient(clientUri);
        MongoDatabase database = client.getDatabase(clientUri.getDatabase());

        try {
            Engine engine = new SingleThreadedEngine(database);

            processRequest(engine, ClientUtils.newDefaultQuery(getPulpFiction(database)));
            processRequest(engine, ClientUtils.newDefaultQuery(getBradPitt(database)));
            processRequest(engine, ClientUtils.newDefaultQuery(getQuentinTarantino(database)));
        } finally {
            client.close();
        }
    }
}
