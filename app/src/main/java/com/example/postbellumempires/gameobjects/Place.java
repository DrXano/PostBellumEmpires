package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.ExpReward;
import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.enums.PlaceType;
import com.example.postbellumempires.enums.Structure;
import com.example.postbellumempires.enums.UnitType;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Place {

    @Exclude
    private static final int MAX_CAPACITY_BY_DEFAULT = 50;

    @Exclude
    private static final double ENEMY_PENALTY = 0.5;

    @Exclude
    private static final double OWNER_BONUS = 1.5;

    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String owner;
    private Faction ownerFaction;
    private PlaceType type;
    private PlaceArmy army;
    private Structure struct1;
    private Structure struct2;
    private Structure struct3;
    private Structure struct4;
    private PlaceBonuses bonuses;

    public Place() {
    }

    public Place(String name, double latitude, double longitude, PlaceType type) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.owner = null;
        this.ownerFaction = null;
        this.type = type;
        this.army = new PlaceArmy(MAX_CAPACITY_BY_DEFAULT);
        this.struct1 = Structure.NONE;
        this.struct2 = Structure.NONE;
        this.struct3 = Structure.NONE;
        this.struct4 = Structure.NONE;
        this.bonuses = new PlaceBonuses(this);
        this.id = this.generateId();
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
        if (ownerFaction == null) {
            return null;
        } else {
            return ownerFaction.name;
        }
    }

    public void setOwnerFaction(String ownerFaction) {
        if (ownerFaction == null) {
            this.ownerFaction = null;
        } else {
            this.ownerFaction = Faction.valueOfName(ownerFaction);
        }
    }


    public String getType() {
        if (type == null) {
            return null;
        } else {
            return type.name();
        }
    }

    public void setType(String type) {
        if (type == null) {
            this.type = null;
        } else {
            this.type = PlaceType.valueOf(type);
        }
    }

    public PlaceArmy getArmy() {
        return army;
    }

    public void setArmy(PlaceArmy army) {
        this.army = army;
    }

    public String getStruct1() {
        return struct1.name();
    }

    public void setStruct1(String struct1) {
        this.struct1 = Structure.valueOf(struct1);
    }

    public String getStruct2() {
        return struct2.name();
    }

    public void setStruct2(String struct2) {
        this.struct2 = Structure.valueOf(struct2);
    }

    public String getStruct3() {
        return struct3.name();
    }

    public void setStruct3(String struct3) {
        this.struct3 = Structure.valueOf(struct3);
    }

    public String getStruct4() {
        return struct4.name;
    }

    public void setStruct4(String struct4) {
        this.struct4 = Structure.valueOf(struct4);
    }

    public PlaceBonuses getBonuses() {
        return bonuses;
    }

    public void setBonuses(PlaceBonuses bonuses) {
        this.bonuses = bonuses;
    }

    @Exclude
    public int getMaxCapacity() {
        return this.army.getMaxCapacity();
    }

    @Exclude
    public int getCapacity() {
        return this.army.getSize();
    }

    @Exclude
    public void free() {
        this.owner = null;
        this.ownerFaction = null;
        this.army.killArmy();
        this.army.setMaxCapacity(MAX_CAPACITY_BY_DEFAULT);
        this.struct1 = Structure.NONE;
        this.struct2 = Structure.NONE;
        this.struct3 = Structure.NONE;
        this.struct4 = Structure.NONE;
        this.bonuses.reset();

        this.updatePlace();
    }

    @Exclude
    public void occupy(Player p) {
        this.owner = p.getInGameName();
        this.ownerFaction = p.getPlayerFaction();
        p.giveExp(ExpReward.POST_CLAIMED.reward);
        p.updatePlayer();
        this.updatePlace();
    }

    @Exclude
    public GameResource getResourceRewardType() {
        switch (type) {
            case RESTAURANT:
                return GameResource.FOOD;
            case UNIVERSITY:
                return GameResource.KNOWLEDGE;
            case SHOP:
                return GameResource.WOOD;
            case OTHER:
                return GameResource.IRON;
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
    private String generateId() {
        int hash = 31;
        hash = 31 * hash + this.name.hashCode();
        hash = 31 * hash + (int) latitude;
        hash = 31 * hash + (int) longitude;
        hash = 31 * hash + type.hashCode();
        return "place_" + hash;
    }

    @Exclude
    public MarkerOptions getMarker() {
        MarkerOptions m = new MarkerOptions();
        m.position(new LatLng(latitude, longitude))
                .title(this.id)
                .icon(getMarkerIcon());
        return m;
    }

    @Exclude
    private BitmapDescriptor getMarkerIcon() {
        switch (type) {
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
    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    @Exclude
    public boolean isOccupied() {
        return this.owner != null && this.ownerFaction != null;
    }

    @Exclude
    public boolean remove(GameUnit gu, int quantity) {
        boolean result = this.army.remove(gu, quantity);
        if (result)
            this.updatePlace();
        return result;
    }

    @Exclude
    public boolean deployAll(Player player) {
        boolean result = this.army.deployAll(player,bonuses);
        if (result)
            this.updatePlace();
        return result;
    }

    @Exclude
    public void updatePlace() {
        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference("places").child(this.id);
        placeRef.setValue(this);
    }


    @Exclude
    public DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference("places").child(this.id);
    }

    @Exclude
    public boolean deploy(Player player, GameUnit[] toDeploy) {
        boolean success = this.army.deploy(player, toDeploy, bonuses);
        if (success)
            this.updatePlace();
        return success;
    }

    @Exclude
    public boolean buildStructure(Structure struct, int pos) {
        boolean result = false;
        switch (pos) {
            case 1:
                if (struct1.equals(Structure.NONE)) {
                    struct1 = struct;
                    result = true;
                }
                break;
            case 2:
                if (struct2.equals(Structure.NONE)) {
                    struct2 = struct;
                    result = true;
                }
                break;
            case 3:
                if (struct3.equals(Structure.NONE)) {
                    struct3 = struct;
                    result = true;
                }
                break;
            case 4:
                if (struct4.equals(Structure.NONE)) {
                    struct4 = struct;
                    result = true;
                }
                break;
            default:
                result = false;
        }

        if (result) {
            this.applyBonuses();
            this.updatePlace();
        }
        return result;
    }

    @Exclude
    public boolean isFriendly(Player player) {
        return this.ownerFaction == null || this.ownerFaction.equals(player.getPlayerFaction());
    }

    @Exclude
    public double multiplier(Player player) {
        if (!this.isFriendly(player)) {
            return ENEMY_PENALTY - this.bonuses.getResourcePenalty();
        } else {
            if (this.owner != null && this.owner.equals(player.getInGameName())) {
                return OWNER_BONUS + this.bonuses.getResourceBonus();
            } else {
                return 1.0 + this.bonuses.getResourceBonus();
            }
        }
    }

    @Exclude
    private void applyBonuses() {
        this.struct1.applyBonus(this.bonuses);
        this.struct2.applyBonus(this.bonuses);
        this.struct3.applyBonus(this.bonuses);
        this.struct4.applyBonus(this.bonuses);
        applyBonusesOnArmy();
    }

    @Exclude
    private void applyBonusesOnArmy(){
        this.army.setMaxCapacity(MAX_CAPACITY_BY_DEFAULT + this.bonuses.getCapacityBonus());
        this.army.applyBonuses(this.bonuses);
    }
}
