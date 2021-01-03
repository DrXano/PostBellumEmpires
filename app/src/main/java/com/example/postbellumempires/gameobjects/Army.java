package com.example.postbellumempires.gameobjects;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Army {

    private List<GameUnit> units;
    private int size;
    private int maxCapacity;

    public Army(){}

    public Army(int maxCapacity){
        this.units = new ArrayList<>();
        this.maxCapacity = maxCapacity;
        this.size = 0;
    }

    public List<GameUnit> getUnits() {
        return units;
    }

    public void setUnits(List<GameUnit> units) {
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
    public boolean addUnit(GameUnit u){
        if(this.size + u.getSize() <= this.maxCapacity){
            this.units.add(u);
            this.size += u.getSize();
            return true;
        }else {
            return false;
        }
    }

    @Exclude
    public boolean addUnits(List<GameUnit> ls){
        if(this.sizeOfGroup(ls) <= maxCapacity){
            for(GameUnit u : ls){
                this.units.add(u);
            }
            return true;
        }else{
            return false;
        }
    }

    @Exclude
    private int sizeOfGroup(List<GameUnit> ls){
        int i = 0;
        for(GameUnit u : ls){
            i += u.getSize();
        }
        return i;
    }
}
