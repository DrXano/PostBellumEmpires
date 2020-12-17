package com.example.postbellumempires.scripts;

import com.example.postbellumempires.enums.PlaceType;
import com.example.postbellumempires.gameobjects.Place;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocationsGenerate {
    public static void generatepoints(){
        DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");
        Place p1 = new Place("Ghost restaurant", 38.71571278026983, -9.217613694306705, PlaceType.RESTAURANT);
        Place p2 = new Place("Ghost college", 38.71555035446279, -9.217819671931323, PlaceType.UNIVERSITY);
        Place p3 = new Place("Ghost shop", 38.71550761076802, -9.217420864190045, PlaceType.SHOP);
        LocRef.child(p1.getId()).setValue(p1);
        LocRef.child(p2.getId()).setValue(p2);
        LocRef.child(p3.getId()).setValue(p3);
    }
}
