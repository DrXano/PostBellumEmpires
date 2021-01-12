package com.example.postbellumempires.scripts;

import com.example.postbellumempires.enums.PlaceType;
import com.example.postbellumempires.gameobjects.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LocationsGenerate {
    public static void generatepoints() {
        DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");

        List<Place> places = new ArrayList<>();

        //MrXano
        places.add(new Place("Xano restaurant", 38.71571278026983, -9.217613694306705, PlaceType.RESTAURANT));
        places.add(new Place("Xano college", 38.71555035446279, -9.217819671931323, PlaceType.UNIVERSITY));
        places.add(new Place("Xano shop", 38.71550761076802, -9.217420864190045, PlaceType.PARK));
        places.add(new Place("Xano Mystery Zone", 38.71598420357416, -9.216387702114936, PlaceType.FACTORY));

        //LAC
        places.add(new Place("restaurant", 38.78820585446469, -9.327045428152713, PlaceType.RESTAURANT));
        places.add(new Place("restaurant", 38.787783314556776, -9.327817316476297, PlaceType.PARK));

        //Mileide


        for (Place p : places) {
            LocRef.child(p.getId()).setValue(p);
        }
    }
}
