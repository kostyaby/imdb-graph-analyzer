package io.github.kostyaby.engine;

import com.mongodb.DBRef;
import com.mongodb.client.MongoDatabase;
import io.github.kostyaby.engine.models.Model;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 5/8/16.
 */
public class MultiThreadedEngine implements Engine {
    private final MongoDatabase database;

    private Set<DBRef> visitedNodes = new HashSet<>();
    private Lock visitedNodesLock = new ReentrantLock();
    private Request request;

    private ForkJoinPool executorService;

    public MultiThreadedEngine(MongoDatabase database, ForkJoinPool executorService) {
        Objects.requireNonNull(database);
        Objects.requireNonNull(executorService);

        this.database = database;
        this.executorService = executorService;
    }

    private class ProcessReferencesTask extends RecursiveTask<Void> {
        TraversalState traversalState;

        ProcessReferencesTask(TraversalState traversalState) {
            this.traversalState = traversalState;
        }

        @Override
        protected Void compute() {
            DBRef node = traversalState.getNode();

            List<ProcessReferencesTask> childProcessReferencesTasks = new ArrayList<>();
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
                        ProcessReferencesTask processReferencesTask
                                = new ProcessReferencesTask(
                                new TraversalState(adjacentNode, queryStructure.getQueryStructures()));
                        processReferencesTask.fork();
                        childProcessReferencesTasks.add(processReferencesTask);
                    }
                }
            }

            for (ProcessReferencesTask childProcessReferencesTask : childProcessReferencesTasks) {
                childProcessReferencesTask.join();
            }

            return null;
        }
    }


    @Override
    public Future<List<Model>> processRequest(Request request) throws ExecutionException, InterruptedException {
        Objects.requireNonNull(request);

        this.request = request;
        this.visitedNodes.clear();

        visitedNodes.add(request.getOrigin());
        RecursiveTask<Void> originProcessReferencesTask = new ProcessReferencesTask(
                new TraversalState(request.getOrigin(), request.getQueryStructures()));

        executorService.submit(originProcessReferencesTask);
        originProcessReferencesTask.join();

        return CompletableFuture.completedFuture(visitedNodes.stream()
                .map(dbRef -> ModelFactory.newModel(dbRef, EngineUtils.fetchDocument(database, dbRef)))
                .limit(request.getMaxResponseSize())
                .collect(Collectors.toList()));
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
    }
}
