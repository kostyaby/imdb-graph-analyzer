package io.github.kostyaby.engine;

import com.mongodb.DBRef;

import java.util.List;

/**
 * Created by kostya_by on 5/8/16.
 */
class TraversalState {
    private final DBRef node;
    private final List<Request.QueryStructure> queryStructures;

    TraversalState(DBRef node, List<Request.QueryStructure> queryStructures) {
        this.node = node;
        this.queryStructures = queryStructures;
    }

    DBRef getNode() {
        return node;
    }

    List<Request.QueryStructure> getQueryStructures() {
        return queryStructures;
    }
}
