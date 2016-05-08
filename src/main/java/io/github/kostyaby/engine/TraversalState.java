package io.github.kostyaby.engine;

import com.mongodb.DBRef;

import java.util.List;

/**
 * Created by kostya_by on 5/8/16.
 */
public class TraversalState {
    private final DBRef node;
    private final List<Request.QueryStructure> queryStructures;

    public TraversalState(DBRef node, List<Request.QueryStructure> queryStructures) {
        this.node = node;
        this.queryStructures = queryStructures;
    }

    public DBRef getNode() {
        return node;
    }

    public List<Request.QueryStructure> getQueryStructures() {
        return queryStructures;
    }
}
