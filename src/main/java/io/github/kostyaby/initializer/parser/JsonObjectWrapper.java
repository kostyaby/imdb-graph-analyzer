package io.github.kostyaby.initializer.parser;

import org.json.simple.JSONObject;

/**
 * Created by kostya_by on 3/31/16.
 */
public class JsonObjectWrapper {
    private final JSONObject jsonObject;

    public JsonObjectWrapper(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public double getDouble(String fieldName) {
        return Double.parseDouble(jsonObject.get(fieldName).toString());
    }

    public int getInt(String fieldName) {
        return Integer.parseInt(jsonObject.get(fieldName).toString());
    }

    public String getString(String fieldName) {
        return jsonObject.get(fieldName).toString();
    }
}
