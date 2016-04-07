package io.github.kostyaby.initializer.parser;

import java.util.Map;

/**
 * Created by kostya_by on 4/7/16.
 */
public interface Parser<T> {
    Map<Integer, T> parse();
}
