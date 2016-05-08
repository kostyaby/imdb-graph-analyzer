package io.github.kostyaby.client;

import com.mongodb.DBRef;
import io.github.kostyaby.engine.ReferenceRetriever;
import io.github.kostyaby.engine.Request;

/**
 * Created by kostya_by on 5/8/16.
 */
class ClientUtils {
    static Request.QueryStructure newQueryStructureWithDepth(
            ReferenceRetriever.Type referenceRetrieverType, int depth) {
        Request.QueryStructure.Builder builder = Request.QueryStructure.newBuilder()
                .setReferenceRetrieverType(referenceRetrieverType)
                .setMaxBranchingFactor(Request.Builder.DEFAULT_MAX_BRANCHING_FACTOR * 3);

        if (depth > 1) {
            builder.addQueryStructure(newQueryStructureWithDepth(referenceRetrieverType, depth - 1));
        }

        return builder.build();
    }

    static Request newDefaultQuery(DBRef origin) {
        return Request.newBuilder()
                .setOrigin(origin)
                .addQueryStructure(
                        newQueryStructureWithDepth(ReferenceRetriever.Type.COMMON_REFERENCE_RETRIEVER, 5))
                .build();
    }
}
