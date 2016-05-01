package io.github.kostyaby.engine;

import com.mongodb.DBRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Request {
    private final DBRef origin;
    private final List<ReferenceRetriever.Type> referenceRetrieverTypes;
    private final int maxDistanceFromOrigin;
    private final int maxResponseSize;
    private final int maxBranchingFactor;

    private Request(
            DBRef origin,
            List<ReferenceRetriever.Type> referenceRetrieverTypes,
            int maxDistanceFromOrigin,
            int maxResponseSize,
            int maxBranchingFactor) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(referenceRetrieverTypes);

        this.origin = origin;
        this.referenceRetrieverTypes = referenceRetrieverTypes;
        this.maxDistanceFromOrigin = maxDistanceFromOrigin;
        this.maxResponseSize = maxResponseSize;
        this.maxBranchingFactor = maxBranchingFactor;
    }

    public static class Builder {
        private final static int DEFAULT_MAX_DISTANCE_FROM_ORIGIN = 4;
        private final static int DEFAULT_MAX_RESPONSE_SIZE = 100;
        private final static int DEFAULT_MAX_BRANCHING_FACTOR = 4;

        private DBRef origin;
        private List<ReferenceRetriever.Type> referenceRetrieverTypes = new ArrayList<>();
        private int maxDistanceFromOrigin = DEFAULT_MAX_DISTANCE_FROM_ORIGIN;
        private int maxResponseSize = DEFAULT_MAX_RESPONSE_SIZE;
        private int maxBranchingFactor = DEFAULT_MAX_BRANCHING_FACTOR;

        private Builder() { }

        public Builder setOrigin(DBRef origin) {
            Objects.requireNonNull(origin);

            this.origin = origin;
            return this;
        }

        public Builder setMaxDistanceFromOrigin(int maxDistanceFromOrigin) {
            this.maxDistanceFromOrigin = maxDistanceFromOrigin;

            return this;
        }

        public Builder setMaxResponseSize(int maxResponseSize) {
            this.maxResponseSize = maxResponseSize;

            return this;
        }

        public Builder setMaxBranchingFactor(int maxBranchingFactor) {
            this.maxBranchingFactor = maxBranchingFactor;

            return this;
        }

        public Builder addReferenceRetrieverType(ReferenceRetriever.Type referenceRetrieverType) {
            Objects.requireNonNull(referenceRetrieverType);

            referenceRetrieverTypes.add(referenceRetrieverType);
            return this;
        }

        public Request build() {
            return new Request(
                    origin, referenceRetrieverTypes, maxDistanceFromOrigin, maxResponseSize, maxBranchingFactor);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public DBRef getOrigin() {
        return origin;
    }

    public List<ReferenceRetriever.Type> getReferenceRetrieverTypes() {
        return referenceRetrieverTypes;
    }

    public int getMaxDistanceFromOrigin() {
        return maxDistanceFromOrigin;
    }

    public int getMaxResponseSize() {
        return maxResponseSize;
    }

    public int getMaxBranchingFactor() {
        return maxBranchingFactor;
    }
}
