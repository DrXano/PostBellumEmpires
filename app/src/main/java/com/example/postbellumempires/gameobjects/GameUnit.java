package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class GameUnit {

    private int level;
    private String name;
    private int size;
    private int quantity;
    private UnitType type;
    private Stats stats;

    public GameUnit() {
    }

    public GameUnit(int level, String name, int size, UnitType type, Stats stats) {
        this.level = level;
        this.name = name;
        this.size = size;
        this.quantity = 1;
        this.type = type;
        this.stats = stats;
    }

    public GameUnit(int level, String name, int size, int quantity, UnitType type, Stats stats) {
        this.level = level;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    @Exclude
    public void removeQuantity(int quantity) {
        int quant = this.quantity - quantity;
        this.quantity = Math.max(quant, 0);
    }

    @Exclude
    public void addQuantity(GameUnit unit) {
        if (this.type.equals(unit.getEType()) && this.level == unit.getLevel()) {
            this.quantity += unit.getQuantity();
        }
    }

    @Exclude
    public void removeQuantity(GameUnit unit) {
        if (this.type.equals(unit.getEType()) && this.level == unit.getLevel()) {
            int quant = this.quantity - unit.getQuantity();
            this.quantity = Math.max(quant, 0);
        }
    }

    @Exclude
    public boolean allKilled() {
        return this.quantity <= 0;
    }

    @Exclude
    public int getTotalSize() {
        return this.quantity * this.size;
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
    public Unit getUnit() {
        return UnitFactory.getUnit(this.type, this.level, this.stats);
    }

    @Exclude
    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        for (int i = 1; i <= this.quantity; i++) {
            units.add(UnitFactory.getUnit(this.type, this.level, this.stats));
        }
        return units;
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

    @Exclude
    @Override
    public String toString() {
        return "Level" + this.level + "_" + this.name;
    }

    @Exclude
    public GameUnit clone() {
        return new GameUnit(this.level, this.name, this.size, this.quantity, this.type, this.stats);
    }
}
