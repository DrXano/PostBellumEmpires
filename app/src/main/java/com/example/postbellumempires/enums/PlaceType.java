package com.example.postbellumempires.enums;

public enum PlaceType {
    RESTAURANT("Restaurant", GameResource.FOOD),
    UNIVERSITY("University", GameResource.KNOWLEDGE),
    SHOP("Shop", GameResource.WOOD),
    OTHER("Other", GameResource.IRON);

    public final String name;
    public final GameResource resource;

    PlaceType(String name, GameResource resource) {
        this.name = name;
        this.resource = resource;
    }
}
