package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.ReferenceRetriever;
import io.github.kostyaby.engine.Request;
import io.github.kostyaby.engine.SingleThreadedEngine;
import io.github.kostyaby.engine.models.Model;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Client {
    public static DBRef getPulpFiction(MongoDatabase database) {
        return new DBRef(
                "movies",
                database.getCollection("movies").find(eq("title", "Pulp Fiction (1994)")).first().getObjectId("_id"));
    }

    public static DBRef getBradPitt(MongoDatabase database) {
        return new DBRef(
                "actors",
                database.getCollection("actors").find(eq("name", "Pitt, Brad")).first().getObjectId("_id"));
    }

    public static DBRef getQuentinTarantino(MongoDatabase database) {
        return new DBRef(
                "directors",
                database.getCollection("directors").find(eq("name", "Tarantino, Quentin")).first().getObjectId("_id"));
    }

    public static void processRequest(Engine engine, Request request) {
        for (Model model : engine.processRequest(request)) {
            System.err.println(model.toPrettyString());
        }

        System.err.println("****************************");
    }

    public static void main(String[] args) {
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

            // Pulp Fiction
            processRequest(engine, Request.newBuilder()
                    .setOrigin(getPulpFiction(database))
                    .addReferenceRetrieverType(ReferenceRetriever.Type.ACTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.DIRECTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.MOVIES_REFERENCE_RETRIEVER)
                    .build());

            // Brad Pitt
            processRequest(engine, Request.newBuilder()
                    .setOrigin(getBradPitt(database))
                    .addReferenceRetrieverType(ReferenceRetriever.Type.ACTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.DIRECTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.MOVIES_REFERENCE_RETRIEVER)
                    .build());

            // Quentin Tarantino
            processRequest(engine, Request.newBuilder()
                    .setOrigin(getQuentinTarantino(database))
                    .addReferenceRetrieverType(ReferenceRetriever.Type.ACTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.DIRECTORS_REFERENCE_RETRIEVER)
                    .addReferenceRetrieverType(ReferenceRetriever.Type.MOVIES_REFERENCE_RETRIEVER)
                    .build());
        } finally {
            client.close();
        }
    }
}
