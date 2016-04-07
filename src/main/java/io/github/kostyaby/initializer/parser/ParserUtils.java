package io.github.kostyaby.initializer.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kostya_by on 4/7/16.
 */
public class ParserUtils {
    public static List<JsonObjectWrapper> parseJsonArray(Reader reader) {
        return (List<JsonObjectWrapper>) ((JSONArray) JSONValue.parse(reader)).stream()
                .map(jsonObject -> new JsonObjectWrapper((JSONObject) jsonObject))
                .collect(Collectors.toList());
    }

    public static <K, V> void putToMultimap(Map<K, List<V>> map, K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<>());
        }

        map.get(key).add(value);
    }

    public static <K, V> Map<V, List<K>> reverseMultimap(Map<K, List<V>> map) {
        Map<V, List<K>> reversedMap = new HashMap<>();

        map.entrySet().forEach(
                mapEntry -> mapEntry.getValue().forEach(
                        value -> putToMultimap(reversedMap, value, mapEntry.getKey())));

        return reversedMap;
    }
}
