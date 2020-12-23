package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.enums.Item;
import com.example.postbellumempires.enums.PlaceType;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Place {

    @Exclude
    private static int MAX_CAPACITY_BY_DEFAULT = 50;

    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String owner;
    private Faction ownerFaction;
    private PlaceType type;
    private int maxCapacity;
    private int capacity;

    public Place(){}

    public Place(String name, double latitude, double longitude, PlaceType type) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.owner = null;
        this.ownerFaction = null;
        this.type = type;
        this.maxCapacity = MAX_CAPACITY_BY_DEFAULT;
        this.capacity = 0;
        this.id = this.id();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerFaction() {
        if(ownerFaction == null){
            return null;
        }else{
            return ownerFaction.name;
        }
    }

    public void setOwnerFaction(String ownerFaction) {
        if(ownerFaction == null){
            this.ownerFaction = null;
        }else{
            this.ownerFaction = Faction.valueOfName(ownerFaction);
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

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Exclude
    public void free(){
        this.owner = null;
        this.ownerFaction = null;
    }

    @Exclude
    public void occupy(Player p){
        this.owner = p.getInGameName();
        this.ownerFaction = p.getPlayerFaction();

        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference("places").child(this.id);
        placeRef.setValue(this);
    }

    @Exclude
    public Item getResourceRewardType(){
        switch (type){
            case RESTAURANT:
                return Item.FOOD;
            case UNIVERSITY:
                return Item.KNOWLEDGE;
            case SHOP:
                return Item.WOOD;
            case OTHER:
                return Item.IRON;
            default:
                return null;
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
    public String id() {
        int hash = 31;
        hash = 31 * hash + this.name.hashCode();
        hash = 31 * hash + (int) latitude;
        hash = 31 * hash + (int) longitude;
        hash = 31 * hash + type.hashCode();
        return "place_"+hash;
    }

    @Exclude
    public MarkerOptions getMarker(){
        MarkerOptions m = new MarkerOptions();
        m.position(new LatLng(latitude,longitude))
                .title(this.id)
                .icon(getMarkerIcon());
        return m;
    }

    @Exclude
    private BitmapDescriptor getMarkerIcon() {
        switch (type){
            case SHOP:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            case RESTAURANT:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            case UNIVERSITY:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
            case OTHER:
                return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
            default:
                return BitmapDescriptorFactory.defaultMarker();
        }
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
