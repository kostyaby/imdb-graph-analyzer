package io.github.kostyaby.engine;

import io.github.kostyaby.engine.models.Model;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by kostya_by on 4/16/16.
 */
public interface Engine extends Closeable {
    Future<List<Model>> processRequest(Request request) throws ExecutionException, InterruptedException;
}
