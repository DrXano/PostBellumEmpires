package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;
import com.google.firebase.database.Exclude;

public class GameUnit {

    private int level;
    private String name;
    private int size;
    private UnitType type;
    private Stats stats;

    public GameUnit() {
    }

    public GameUnit(int level, String name, int size, UnitType type, Stats stats) {
        this.level = level;
        this.name = name;
        this.size = size;
        this.type = type;
        this.stats = stats;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type.name();
    }

    public void setType(String type) {
        this.type = UnitType.valueOf(type);
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Exclude
    public UnitType getEType() {
        return type;
    }

    @Exclude
    public void setEType(UnitType type) {
        this.type = type;
    }

    @Exclude
    public Unit getUnit(){
        return UnitFactory.getUnit(this.type,this.level,this.stats);
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameUnit gameUnit = (GameUnit) o;
        return level == gameUnit.level &&
                size == gameUnit.size &&
                name.equals(gameUnit.name) &&
                type == gameUnit.type &&
                stats.equals(gameUnit.stats);
    }
}
