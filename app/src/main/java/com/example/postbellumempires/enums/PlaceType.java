package com.example.postbellumempires.enums;

public enum PlaceType {
    RESTAURANT("Restaurant", GameResource.FOOD),
    CAFE("Cafe", GameResource.FOOD),
    BAKERY("Bakery", GameResource.FOOD),
    UNIVERSITY("University", GameResource.KNOWLEDGE),
    MUSEUM("Museum", GameResource.KNOWLEDGE),
    LIBRARY("Library", GameResource.BP),
    HOSPITAL("Hospital", GameResource.MEDICINE),
    PHARMACY("Pharmacy", GameResource.MEDICINE),
    PARK("Park", GameResource.WOOD),
    FACTORY("Factory", GameResource.IRON),
    TRAINSTATION("Train Station", GameResource.IRON),
    SUBWAY("Subway", GameResource.IRON),
    BEACH("Beach", GameResource.STONE),
    CASTLE("Castle", GameResource.STONE),
    CASINO("Casino", GameResource.URANIUM);

    public final String name;
    public final GameResource resource;

    PlaceType(String name, GameResource resource) {
        this.name = name;
        this.resource = resource;
    }
}
