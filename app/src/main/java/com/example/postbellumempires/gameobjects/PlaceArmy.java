package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceArmy {

    private HashMap<String, GameUnit> units;
    private int size;
    private int maxCapacity;

    public PlaceArmy() {
    }

    public PlaceArmy(int maxCapacity) {
        this.units = new HashMap<>();
        this.maxCapacity = maxCapacity;
        this.size = 0;
    }

    public HashMap<String, GameUnit> getUnits() {
        return units;
    }

    public void setUnits(HashMap<String, GameUnit> units) {
        this.units = units;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Exclude
    public boolean add(GameUnit gu) {
        if (this.units == null)
            this.units = new HashMap<>();

        if (gu.getTotalSize() <= this.availableCapacity()) {
            if (this.units.containsKey(gu.toString())) {
                GameUnit guu = this.units.get(gu.toString());
                guu.addQuantity(gu);
                this.units.put(gu.toString(), guu);
            } else {
                this.units.put(gu.toString(), gu);
            }
            this.size += gu.getTotalSize();
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    public boolean add(GameUnit gu, int quantity) {
        if (this.units == null)
            this.units = new HashMap<>();

        if (gu == null)
            return false;

        if (gu.getSize() * quantity <= this.availableCapacity()) {
            if (this.units.containsKey(gu.toString())) {
                GameUnit guu = this.units.get(gu.toString());
                guu.addQuantity(quantity);
                this.units.put(gu.toString(), guu);
            } else {
                GameUnit guu = gu.clone();
                guu.setQuantity(quantity);
                this.units.put(gu.toString(), guu);
            }
            this.size += gu.getSize() * quantity;
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    public boolean remove(GameUnit gu, int quantity) {
        if (this.units == null)
            return false;

        if (gu == null)
            return false;

        if (!this.units.containsKey(gu.toString())) {
            return false;
        }

        GameUnit guu = this.units.get(gu.toString());
        int quant = Math.min(quantity, guu.getQuantity());
        guu.removeQuantity(quant);

        this.size -= guu.getSize() * quant;
        if (this.size < 0)
            this.size = 0;

        if (guu.allKilled()) {
            this.units.remove(gu.toString());
        } else {
            this.units.put(gu.toString(), guu);
        }
        return true;
    }

    @Exclude
    public boolean deployAll(Player p) {
        PlayerArmy pa = p.getArmy();
        if (pa.getSize() <= this.availableCapacity()) {
            for (GameUnit gu : pa.getUnits().values()) {
                this.add(gu);
            }
            p.emptyArmy();
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    public boolean deployUnit(Player p, UnitType type, int quantity) {
        PlayerArmy pa = p.getArmy();
        if (!pa.isUnitAvailable(type))
            return false;

        GameUnit gu = pa.get(type);

        if (gu == null)
            return false;

        if (gu.getSize() * quantity <= this.availableCapacity()) {
            this.add(gu, quantity);
            p.removeUnit(type, quantity);
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    public GameUnit[] getUnitsArray(){
        List<GameUnit> units = new ArrayList<>(this.units.values());
        return units.toArray(new GameUnit[units.size()]);
    }

    @Exclude
    public int availableCapacity() {
        return this.maxCapacity - this.size;
    }

    @Exclude
    public void killArmy() {
        this.units = new HashMap<>();
        this.size = 0;
    }
}
