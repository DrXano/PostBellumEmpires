package com.example.postbellumempires.scripts;

import com.example.postbellumempires.enums.PlaceType;
import com.example.postbellumempires.gameobjects.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LocationsGenerate {
    public static void generatepoints(){
        DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");

        List<Place> places = new ArrayList<>();
        places.add(new Place("Ghost restaurant", 38.71571278026983, -9.217613694306705, PlaceType.RESTAURANT));
        places.add(new Place("Ghost college", 38.71555035446279, -9.217819671931323, PlaceType.UNIVERSITY));
        places.add(new Place("Ghost shop", 38.71550761076802, -9.217420864190045, PlaceType.SHOP));



        places.add(new Place("CV restaurant", 39.41403321231397, -7.457877936488816, PlaceType.RESTAURANT));
        places.add(new Place("CV college", 39.41352436926469, -7.45827632584793, PlaceType.UNIVERSITY));
        places.add(new Place("CV shop", 39.41368074581658, -7.4572707139979055, PlaceType.SHOP));

        for(Place p: places){
            LocRef.child(p.getId()).setValue(p);
        }
    }
}
