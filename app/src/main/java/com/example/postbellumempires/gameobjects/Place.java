package com.example.postbellumempires.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import com.example.postbellumempires.R;
import com.example.postbellumempires.enums.ExpReward;
import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.enums.PlaceType;
import com.example.postbellumempires.enums.Structure;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Place implements Serializable {

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
    private boolean underAttack;

    public Place() {
    }

    public Place(String id, String name, double latitude, double longitude, PlaceType type) {
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
        this.underAttack = false;
        this.id = id;
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
        return struct4.name();
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
        this.struct1 = Structure.NONE;
        this.struct2 = Structure.NONE;
        this.struct3 = Structure.NONE;
        this.struct4 = Structure.NONE;
        this.bonuses.reset();
        this.army.killArmy();
        this.army.setMaxCapacity(MAX_CAPACITY_BY_DEFAULT);

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
        return this.type.resource;
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
    public MarkerOptions getMarker(Resources resources) {
        MarkerOptions m = new MarkerOptions();
        m.position(new LatLng(latitude, longitude))
                .title(this.id)
                .icon(getMarkerIcon(resources));
        return m;
    }

    @Exclude
    public BitmapDescriptor getMarkerIcon(Resources resources) {
        BitmapDrawable bitmap = (BitmapDrawable) resources.getDrawable(this.type.icon);
        Bitmap markerBitmap = Bitmap.createScaledBitmap(bitmap.getBitmap(), 120, 200, false);

        int colorId;
        if (this.underAttack) {
            colorId = R.color.underattack;
        } else {
            if (this.ownerFaction == null) {
                colorId = R.color.neutralized;
            } else {
                colorId = this.ownerFaction.primaryColor;
            }
        }

        Bitmap resultBitmap = markerBitmap.copy(markerBitmap.getConfig(), true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(resources.getColor(colorId), 0);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return BitmapDescriptorFactory.fromBitmap(resultBitmap);
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
        boolean result = this.army.deployAll(player, bonuses);
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
    public Structure getEStruct1() {
        return this.struct1;
    }

    @Exclude
    public Structure getEStruct2() {
        return this.struct2;
    }

    @Exclude
    public Structure getEStruct3() {
        return this.struct3;
    }

    @Exclude
    public Structure getEStruct4() {
        return this.struct4;
    }

    @Exclude
    private void applyBonuses() {
        this.bonuses.reset();
        this.struct1.applyBonus(this.bonuses);
        this.struct2.applyBonus(this.bonuses);
        this.struct3.applyBonus(this.bonuses);
        this.struct4.applyBonus(this.bonuses);
        applyBonusesOnArmy();
    }

    @Exclude
    private void applyBonusesOnArmy() {
        this.army.setMaxCapacity(MAX_CAPACITY_BY_DEFAULT + this.bonuses.getCapacityBonus());
        this.army.applyBonuses(this.bonuses);
    }

    @Exclude
    public void removeUnits(GameUnit[] toRemove) {
        this.army.removeUnits(toRemove);
    }

    @Exclude
    public void remove(BattleUnit bu) {
        this.army.remove(bu);
    }

    public boolean getUnderAttack() {
        return underAttack;
    }

    public void setUnderAttack(boolean underAttack) {
        this.underAttack = underAttack;
    }
}
