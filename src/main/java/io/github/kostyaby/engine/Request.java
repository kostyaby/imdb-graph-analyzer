package io.github.kostyaby.engine;

import com.mongodb.DBRef;

/**
 * Created by kostya_by on 4/16/16.
 */
public class Request {
    private final DBRef origin;

    private Request(DBRef origin) {
        this.origin = origin;
    }

    public static class Builder {
        private DBRef origin;

        private Builder() { }

        public Builder setOrigin(DBRef origin) {
            this.origin = origin;

            return this;
        }

        public Request build() {
            return new Request(origin);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public DBRef getOrigin() {
        return origin;
    }
}
