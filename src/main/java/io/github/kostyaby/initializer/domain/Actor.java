package io.github.kostyaby.initializer.domain;

/**
 * Created by kostya_by on 3/31/16.
 */
public class Actor {
    private final String name;
    private final String gender;

    public Actor(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }
}
