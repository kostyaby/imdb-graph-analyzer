package io.github.kostyaby.engine;

import io.github.kostyaby.engine.models.Model;

import java.util.List;

/**
 * Created by kostya_by on 4/16/16.
 */
public interface Engine {
    List<Model> processRequest(Request request);
}
