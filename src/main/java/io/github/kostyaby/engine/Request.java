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
    private final int distanceFromOriginLimit;
    private final int responseSizeLimit;

    private Request(
            DBRef origin,
            List<ReferenceRetriever.Type> referenceRetrieverTypes,
            int distanceFromOriginLimit,
            int responseSizeLimit) {
        Objects.requireNonNull(origin);
        Objects.requireNonNull(referenceRetrieverTypes);

        this.origin = origin;
        this.referenceRetrieverTypes = referenceRetrieverTypes;
        this.distanceFromOriginLimit = distanceFromOriginLimit;
        this.responseSizeLimit = responseSizeLimit;
    }

    public static class Builder {
        private final static int DEFAULT_DISTANCE_FROM_ORIGIN_LIMIT = 5;
        private final static int DEFAULT_RESPONSE_SIZE_LIMIT = 50;

        private DBRef origin;
        private List<ReferenceRetriever.Type> referenceRetrieverTypes = new ArrayList<>();
        private int distanceFromOriginLimit = DEFAULT_DISTANCE_FROM_ORIGIN_LIMIT;
        private int responseSizeLimit = DEFAULT_RESPONSE_SIZE_LIMIT;

        private Builder() { }

        public Builder setOrigin(DBRef origin) {
            Objects.requireNonNull(origin);

            this.origin = origin;
            return this;
        }

        public Builder setDistanceFromOriginLimit(int distanceFromOriginLimit) {
            this.distanceFromOriginLimit = distanceFromOriginLimit;

            return this;
        }

        public Builder setResponseSizeLimit(int responseSizeLimit) {
            this.responseSizeLimit = responseSizeLimit;

            return this;
        }

        public Builder addReferenceRetrieverType(ReferenceRetriever.Type referenceRetrieverType) {
            Objects.requireNonNull(referenceRetrieverType);

            referenceRetrieverTypes.add(referenceRetrieverType);
            return this;
        }

        public Request build() {
            return new Request(origin, referenceRetrieverTypes, distanceFromOriginLimit, responseSizeLimit);
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

    public int getDistanceFromOriginLimit() {
        return distanceFromOriginLimit;
    }

    public int getResponseSizeLimit() {
        return responseSizeLimit;
    }
}
