package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;
import com.example.postbellumempires.gameobjects.units.Marine;
import com.example.postbellumempires.gameobjects.units.Pyro;
import com.example.postbellumempires.gameobjects.units.Tank;

public class UnitFactory {

    public static Unit getUnit(UnitType type) {
        return getUnitAux(type, 0);
    }

    public static Unit getUnit(UnitType type, int level) {
        return getUnitAux(type, level);
    }

    public static Unit getUnit(UnitType type, int level, Stats stats) {
        Unit u = getUnitAux(type, level);
        if (stats != null)
            u.setStats(stats);
        return u;
    }

    private static Unit getUnitAux(UnitType type, int level) {
        switch (type) {
            case MARINE:
                return new Marine(level);
            case PYRO:
                return new Pyro(level);
            case TANK:
                return new Tank(level);
            default:
                return null;
        }
    }
}
