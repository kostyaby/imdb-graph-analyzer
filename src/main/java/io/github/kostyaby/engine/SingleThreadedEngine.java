package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.models.Model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/16/16.
 */
public class SingleThreadedEngine implements Engine {
    private final MongoDatabase database;

    public SingleThreadedEngine(MongoDatabase database) {
        Objects.requireNonNull(database);

        this.database = database;
    }

    @Override
    public List<Model> processRequest(Request request) {
        Objects.requireNonNull(request);

        Map<DBRef, Integer> distancesFromOrigin = new HashMap<>();
        Queue<DBRef> queue = new LinkedList<>();

        queue.add(request.getOrigin());
        distancesFromOrigin.put(request.getOrigin(), 0);

        while (!queue.isEmpty()) {
            DBRef node = queue.poll();
            int distanceFromOrigin = distancesFromOrigin.get(node);

            if (distanceFromOrigin >= request.getDistanceFromOriginLimit()
                    || distancesFromOrigin.size() >= request.getResponseSizeLimit()) {
                continue;
            }

            for (ReferenceRetriever.Type referenceRetrieverType : request.getReferenceRetrieverTypes()) {
                ReferenceRetriever referenceRetriever = ReferenceRetrieverFactory.newReferenceRetriever(
                        database, referenceRetrieverType);
                for (DBRef adjacentNode : referenceRetriever.retrieveReferences(node)) {
                    if (distancesFromOrigin.containsKey(adjacentNode)
                            || distancesFromOrigin.size() >= request.getResponseSizeLimit()) {
                        continue;
                    }

                    distancesFromOrigin.put(adjacentNode, distanceFromOrigin + 1);
                    queue.add(adjacentNode);
                }
            }
        }

        return distancesFromOrigin.keySet().stream()
                .map(dbRef -> ModelFactory.newModel(dbRef, EngineUtils.fetchDocument(database, dbRef)))
                .collect(Collectors.toList());
    }
}
