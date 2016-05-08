package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.models.Model;
import org.apache.commons.lang3.tuple.Pair;

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

        Set<DBRef> visitedNodes = new HashSet<>();
        Queue<TraversalState> queue = new LinkedList<>();

        queue.add(new TraversalState(request.getOrigin(), request.getQueryStructures()));
        visitedNodes.add(request.getOrigin());

        while (!queue.isEmpty()) {
            TraversalState traversalState = queue.poll();

            if (visitedNodes.size() >= request.getMaxResponseSize()) {
                break;
            }

            DBRef node = traversalState.getNode();

            for (Request.QueryStructure queryStructure : traversalState.getQueryStructures()) {
                ReferenceRetriever referenceRetriever = ReferenceRetrieverFactory.newReferenceRetriever(
                        database, queryStructure.getReferenceRetrieverType());

                int maxBranchingFactor = queryStructure.getMaxBranchingFactor();
                if (maxBranchingFactor == -1) {
                    maxBranchingFactor = request.getMaxBranchingFactor();
                }

                for (DBRef adjacentNode : referenceRetriever.retrieveReferences(node, maxBranchingFactor)) {
                    if (visitedNodes.size() >= request.getMaxResponseSize()) {
                        break;
                    }

                    if (!visitedNodes.contains(adjacentNode)) {
                        visitedNodes.add(adjacentNode);
                        queue.add(new TraversalState(adjacentNode, queryStructure.getQueryStructures()));
                    }
                }
            }
        }

        return CompletableFuture.completedFuture(visitedNodes.stream()
                .map(dbRef -> ModelFactory.newModel(dbRef, EngineUtils.fetchDocument(database, dbRef)))
                .collect(Collectors.toList()));
    }

    @Override
    public void close() {
        // do nothing
    }
}
