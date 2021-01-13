package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.ExpReward;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceArmy implements Serializable {

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
    public boolean add(GameUnit gu, PlaceBonuses bonuses) {
        if (this.units == null)
            this.units = new HashMap<>();

        if (gu.getTotalSize() <= this.availableCapacity()) {
            GameUnit guu;
            if (this.units.containsKey(gu.toString())) {
                guu = this.units.get(gu.toString());
                guu.addQuantity(gu);
            } else {
                guu = gu.clone();
            }
            if (bonuses != null)
                guu.applyBonuses(bonuses);
            this.units.put(gu.toString(), guu);
            this.size += gu.getTotalSize();
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
    public void removeUnits(GameUnit[] toRemove) {
        for (GameUnit gu : toRemove) {
            this.remove(gu, gu.getQuantity());
        }
    }

    @Exclude
    public boolean deployAll(Player p, PlaceBonuses bonuses) {
        PlayerArmy pa = p.getArmy();
        if (pa.getSize() <= this.availableCapacity()) {
            int exp = 0;
            for (GameUnit gu : pa.getAvailableUnits()) {
                this.add(gu, bonuses);
                exp += gu.getQuantity() * ExpReward.UNIT_DEPLOYED.reward;
            }
            p.giveExp(exp);
            p.emptyArmy();
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    public GameUnit[] getUnitsArray() {
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

    @Exclude
    public boolean deploy(Player player, GameUnit[] toDeploy, PlaceBonuses bonuses) {
        if (calculateSize(toDeploy) <= this.availableCapacity()) {
            int exp = 0;
            for (GameUnit gu : toDeploy) {
                if (gu.getQuantity() > 0) {
                    this.add(gu, bonuses);
                    exp += gu.getQuantity() * ExpReward.UNIT_DEPLOYED.reward;
                }
            }
            player.giveExp(exp);
            player.removeUnits(toDeploy);
            return true;
        } else {
            return false;
        }
    }

    @Exclude
    private int calculateSize(GameUnit[] toDeploy) {
        int total = 0;
        for (GameUnit gu : toDeploy)
            total += gu.getTotalSize();
        return total;
    }

    @Exclude
    public void applyBonuses(PlaceBonuses bonuses) {
        if (this.units != null) {
            for (String key : this.units.keySet()) {
                GameUnit gu = this.units.get(key);
                gu.applyBonuses(bonuses);
                this.units.put(key, gu);
            }
        }
    }

    @Exclude
    public void remove(BattleUnit battleUnit) {
        GameUnit gu = battleUnit.toGameUnit();
        this.remove(gu,1);
    }
}
