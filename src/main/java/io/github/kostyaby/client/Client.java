package io.github.kostyaby.client;

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.Engine;
import io.github.kostyaby.engine.Request;
import io.github.kostyaby.engine.SingleThreadEngine;
import io.github.kostyaby.engine.models.Model;
import org.bson.types.ObjectId;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Client {
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
            Engine engine = new SingleThreadEngine(database);

            // Pulp Fiction
            Request request = Request.newBuilder()
                    .setOrigin(new DBRef("movies", new ObjectId("57068d1179a3fe62cd99f7a3"))).build();

            for (Model model : engine.processRequest(request)) {
                System.err.println(model.toPrettyString());
            }
        } finally {
            client.close();
        }
    }
}
