package com.example.postbellumempires.enums;

import com.example.postbellumempires.R;

public enum PlaceType {
    RESTAURANT("Restaurant", GameResource.FOOD, R.drawable.icon_restaurant),
    CAFE("Cafe", GameResource.FOOD, R.drawable.icon_cafe),
    BAKERY("Bakery", GameResource.FOOD, R.drawable.icon_bakery),
    UNIVERSITY("University", GameResource.KNOWLEDGE, R.drawable.icon_university),
    MUSEUM("Museum", GameResource.KNOWLEDGE, R.drawable.icon_museum),
    LIBRARY("Library", GameResource.BP, R.drawable.icon_library),
    HOSPITAL("Hospital", GameResource.MEDICINE, R.drawable.icon_hospital),
    PHARMACY("Pharmacy", GameResource.MEDICINE, R.drawable.icon_pharmacy),
    PARK("Park", GameResource.WOOD, R.drawable.icon_park),
    FACTORY("Factory", GameResource.IRON, R.drawable.icon_factory),
    TRAINSTATION("Train Station", GameResource.IRON, R.drawable.icon_trainstation),
    SUBWAY("Subway", GameResource.IRON, R.drawable.icon_subway),
    BEACH("Beach", GameResource.STONE, R.drawable.icon_beach),
    CASTLE("Castle", GameResource.STONE, R.drawable.icon_castle),
    CASINO("Casino", GameResource.URANIUM, R.drawable.icon_casino);

    public final String name;
    public final GameResource resource;
    public final int icon;

    PlaceType(String name, GameResource resource, int icon) {
        this.name = name;
        this.resource = resource;
        this.icon = icon;
    }
}
