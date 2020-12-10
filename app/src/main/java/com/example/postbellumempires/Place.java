package com.example.postbellumempires;

import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.enums.PlaceType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.Exclude;

public class Place {

    private String name;
    private double latitude;
    private double longitude;
    private String Owner;
    private Faction ownerFaction;
    private PlaceType type;

    public Place(){}

    public Place(String name, double latitude, double longitude, String owner, Faction ownerFaction, PlaceType type) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        Owner = owner;
        this.ownerFaction = ownerFaction;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getOwnerFaction() {
        if(ownerFaction == null){
            return null;
        }else{
            return ownerFaction.name();
        }
    }

    public void setOwnerFaction(String ownerFaction) {
        if(ownerFaction == null){
            this.ownerFaction = null;
        }else{
            this.ownerFaction = Faction.valueOf(ownerFaction);
        }
    }


    public String getType() {
        if(type == null){
            return null;
        }else{
            return type.name();
        }
    }

    public void setType(String type) {
        if(type == null){
            this.type = null;
        }else{
            this.type = PlaceType.valueOf(type);
        }
    }

    @Exclude
    public Faction getFaction() {
        return ownerFaction;
    }

    @Exclude
    public void setFaction(Faction ownerFaction) {
        this.ownerFaction = ownerFaction;
    }

    @Exclude
    public MarkerOptions getMarker(){
        MarkerOptions m = new MarkerOptions();
        m.position(new LatLng(latitude,longitude))
                .title(name);
        return m;
    }

    @Exclude
    public PlaceType getPType() {
        return type;
    }

    @Exclude
    public void setPType(PlaceType type) {
        this.type = type;
    }

    @Exclude
    public LatLng getLatLng(){
        return new LatLng(latitude,longitude);
    }
}
