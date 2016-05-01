package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.models.Model;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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
    public synchronized Future<List<Model>> processRequest(Request request) {
        Objects.requireNonNull(request);

        Map<DBRef, Integer> distancesFromOrigin = new HashMap<>();
        Queue<DBRef> queue = new LinkedList<>();

        DBRef origin = request.getOrigin();
        List<ReferenceRetriever.Type> referenceRetrieverTypes = request.getReferenceRetrieverTypes();
        int maxDistanceFromOrigin = request.getMaxDistanceFromOrigin();
        int maxResponseSize = request.getMaxResponseSize();
        int maxBranchingFactor = request.getMaxBranchingFactor();

        queue.add(origin);
        distancesFromOrigin.put(origin, 0);

        while (!queue.isEmpty()) {
            DBRef node = queue.poll();
            int distanceFromOrigin = distancesFromOrigin.get(node);

            if (distanceFromOrigin >= maxDistanceFromOrigin || distancesFromOrigin.size() >= maxResponseSize) {
                break;
            }

            for (ReferenceRetriever.Type referenceRetrieverType : referenceRetrieverTypes) {
                ReferenceRetriever referenceRetriever = ReferenceRetrieverFactory.newReferenceRetriever(
                        database, referenceRetrieverType);
                for (DBRef adjacentNode : referenceRetriever.retrieveReferences(node, maxBranchingFactor)) {
                    if (distancesFromOrigin.size() >= request.getMaxResponseSize()) {
                        break;
                    }
                    if (!distancesFromOrigin.containsKey(adjacentNode)) {
                        distancesFromOrigin.put(adjacentNode, distanceFromOrigin + 1);
                        queue.add(adjacentNode);
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(distancesFromOrigin.keySet().stream()
                .map(dbRef -> ModelFactory.newModel(dbRef, EngineUtils.fetchDocument(database, dbRef)))
                .collect(Collectors.toList()));
    }

    @Override
    public void close() {
        // do nothing
    }
}
