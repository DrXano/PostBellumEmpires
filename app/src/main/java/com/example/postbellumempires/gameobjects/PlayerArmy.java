package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerArmy {

    private HashMap<String, GameUnit> units;
    private int size;
    private int maxCapacity;

    public PlayerArmy() {
    }

    public PlayerArmy(int maxCapacity) {
        this.units = new HashMap<>();
        this.maxCapacity = maxCapacity;
        this.size = 0;
        for (UnitType t : UnitType.values()) {
            Unit u = UnitFactory.getUnit(t);
            GameUnit gu = u.toGameUnit();
            gu.setQuantity(0);
            units.put(t.name(), gu);
        }

        this.unlock(UnitType.MARINE);
        this.add(UnitType.MARINE, 10);
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
    public boolean add(UnitType type, int quantity) {
        GameUnit gu = units.get(type.name());
        if (gu != null) {
            if (gu.getSize()*quantity  <= this.availableCapacity()) {
                gu.addQuantity(quantity);
                this.size += gu.getSize()*quantity ;
                units.put(type.name(), gu);
                return true;
            } else {
                return false;
            }
        } else {
            Unit u = UnitFactory.getUnit(type);
            if (u.getLevel() == 0) {
                u.levelUp();
            }
            gu = u.toGameUnit();
            gu.setQuantity(quantity);
            if (gu.getSize()*quantity <= maxCapacity) {
                units.put(type.name(), gu);
                this.size += gu.getSize()*quantity;
                return true;
            } else {
                return false;
            }
        }
    }

    @Exclude
    public boolean remove(UnitType type, int quantity) {
        if (units.containsKey(type.name())) {
            GameUnit gu = units.get(type.name());
            int quant = Math.min(quantity, gu.getQuantity());
            gu.removeQuantity(quant);
            this.size -= quant*gu.getSize();
            units.put(type.name(), gu);
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    public GameUnit get(UnitType type) {
        return this.units.get(type.name());
    }

    @Exclude
    public void levelUp(UnitType type) {
        Unit u = units.get(type.name()).getUnit();

        if (u.getLevel() > 0 && !u.isMaxed()) {
            u.levelUp();
            GameUnit gu = u.toGameUnit();
            gu.setQuantity(units.get(type.name()).getQuantity());
            units.put(type.name(), gu);
        }
    }

    @Exclude
    public void unlock(UnitType type) {
        Unit u = units.get(type.name()).getUnit();
        if (u.getLevel() == 0) {
            u.levelUp();
            GameUnit gu = u.toGameUnit();
            gu.setQuantity(units.get(type.name()).getQuantity());
            units.put(type.name(), gu);
        }
    }

    @Exclude
    public void emptyArmy() {
        for (String k : units.keySet()) {
            GameUnit gu = units.get(k);
            gu.setQuantity(0);
            units.put(k, gu);
        }
        this.size = 0;
    }

    @Exclude
    public boolean isEmpty() {
        for (GameUnit gu : units.values()) {
            if (gu.getQuantity() > 0) {
                return false;
            }
        }
        return true;
    }

    @Exclude
    public boolean isUnitAvailable(UnitType type) {
        return this.units.get(type.name()).getQuantity() > 0;
    }

    @Exclude
    public int availableCapacity() {
        return this.maxCapacity - this.size;
    }

    @Exclude
    public List<GameUnit> getAvailableUnits() {
        if (this.units == null && this.isEmpty()) {
            return null;
        } else {
            List<GameUnit> result = new ArrayList<>();
            for (GameUnit gu : this.units.values()) {
                if (!gu.allKilled())
                    result.add(gu);
            }
            return result;
        }
    }
}
