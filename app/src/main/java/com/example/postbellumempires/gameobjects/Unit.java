package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;

public abstract class Unit {

    private final String name;
    private final int size;
    private final Item[] trainCost;
    private final UnitType type;

    public Unit(String name, int size, Item[] trainCost, UnitType type) {
        this.name = name;
        this.size = size;
        this.trainCost = trainCost;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Item[] getCost() {
        return this.trainCost;
    }

    public UnitType getType() {
        return this.type;
    }

    public abstract Stats getStats();

    public abstract void setStats(Stats stats);

    public abstract int getLevel();

    public abstract int getMaxLevel();

    public abstract GameUnit toGameUnit();

    public abstract boolean isMaxed();

    public abstract Item[] getUpgradeCost();

    public abstract void levelUp();

    @Override
    public abstract String toString();
}
