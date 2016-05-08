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
    private final int maxResponseSize;
    private final int maxBranchingFactor;
    private final List<QueryStructure> queryStructures;

    private Request(
            DBRef origin,
            int maxResponseSize,
            int maxBranchingFactor,
            List<QueryStructure> queryStructures) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(queryStructures);

        this.origin = origin;
        this.maxResponseSize = maxResponseSize;
        this.maxBranchingFactor = maxBranchingFactor;
        this.queryStructures = queryStructures;
    }

    public static class QueryStructure {
        private final ReferenceRetrieverType referenceRetrieverType;
        private final int maxBranchingFactor;
        private final List<QueryStructure> queryStructures;

        private QueryStructure(
                ReferenceRetrieverType referenceRetrieverType,
                int maxBranchingFactor,
                List<QueryStructure> queryStructures) {
            Objects.requireNonNull(referenceRetrieverType);
            Objects.requireNonNull(queryStructures);

            this.referenceRetrieverType = referenceRetrieverType;
            this.maxBranchingFactor = maxBranchingFactor;
            this.queryStructures = queryStructures;
        }

        public static class Builder {
            public final static int DEFAULT_MAX_BRANCHING_FACTOR = -1;

            private ReferenceRetrieverType referenceRetrieverType;
            private int maxBranchingFactor = DEFAULT_MAX_BRANCHING_FACTOR;
            private List<QueryStructure> queryStructures = new ArrayList<>();

            private Builder() { }

            public Builder setReferenceRetrieverType(ReferenceRetrieverType referenceRetrieverType) {
                Objects.requireNonNull(referenceRetrieverType);

                this.referenceRetrieverType = referenceRetrieverType;
                return this;
            }

            public Builder setMaxBranchingFactor(int maxBranchingFactor) {
                this.maxBranchingFactor = maxBranchingFactor;
                return this;
            }

            public Builder addQueryStructure(QueryStructure queryStructure) {
                Objects.requireNonNull(queryStructure);

                queryStructures.add(queryStructure);
                return this;
            }

            public QueryStructure build() {
                return new QueryStructure(referenceRetrieverType, maxBranchingFactor, queryStructures);
            }
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public ReferenceRetrieverType getReferenceRetrieverType() {
            return referenceRetrieverType;
        }

        public int getMaxBranchingFactor() {
            return maxBranchingFactor;
        }

        public List<QueryStructure> getQueryStructures() {
            return queryStructures;
        }
    }

    public static class Builder {
        public final static int DEFAULT_MAX_RESPONSE_SIZE = 20;
        public final static int DEFAULT_MAX_BRANCHING_FACTOR = 5;

        private DBRef origin;
        private int maxResponseSize = DEFAULT_MAX_RESPONSE_SIZE;
        private int maxBranchingFactor = DEFAULT_MAX_BRANCHING_FACTOR;
        private List<QueryStructure> queryStructures = new ArrayList<>();

        private Builder() { }

        public Builder setOrigin(DBRef origin) {
            Objects.requireNonNull(origin);

            this.origin = origin;
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

        public Builder addQueryStructure(QueryStructure queryStructure) {
            Objects.requireNonNull(queryStructure);

            queryStructures.add(queryStructure);
            return this;
        }

        public Request build() {
            return new Request(
                    origin, maxResponseSize, maxBranchingFactor, queryStructures);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public DBRef getOrigin() {
        return origin;
    }

    public int getMaxResponseSize() {
        return maxResponseSize;
    }

    public int getMaxBranchingFactor() {
        return maxBranchingFactor;
    }

    public List<QueryStructure> getQueryStructures() {
        return queryStructures;
    }
}
