package com.example.postbellumempires.enums;

import java.util.HashMap;
import java.util.Map;

public enum Faction {
    OC("Oratio Cult"),
    DR("Dauntless Raiders"),
    ES("Erudite Sovereignty");

    private static final Map<String, Faction> BY_NAME = new HashMap<>();

    static {
        for (Faction e : values()) {
            BY_NAME.put(e.name, e);
        }
    }

    public final String name;

    Faction(String name) {
        this.name = name;
    }

    public static Faction valueOfName(String name) {
        return BY_NAME.get(name);
    }
}
