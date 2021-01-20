package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;
import com.example.postbellumempires.gameobjects.units.Apprentice;
import com.example.postbellumempires.gameobjects.units.Archer;
import com.example.postbellumempires.gameobjects.units.Assassin;
import com.example.postbellumempires.gameobjects.units.Chemist;
import com.example.postbellumempires.gameobjects.units.Marine;
import com.example.postbellumempires.gameobjects.units.Medic;
import com.example.postbellumempires.gameobjects.units.Pyro;
import com.example.postbellumempires.gameobjects.units.Rambo;
import com.example.postbellumempires.gameobjects.units.Tank;
import com.example.postbellumempires.gameobjects.units.Uav;
import com.example.postbellumempires.gameobjects.units.Wick;

public class UnitFactory {

    public static Unit getUnit(UnitType type) {
        return getUnitAux(type, 0);
    }

    public static Unit getUnit(UnitType type, int level) {
        return getUnitAux(type, level);
    }

    public static Unit getUnit(UnitType type, int level, Stats stats) {
        Unit u = getUnitAux(type, level);
        if (stats != null && u != null)
            u.setStats(stats);
        return u;
    }

    private static Unit getUnitAux(UnitType type, int level) {
        switch (type) {
            case ARCHER:
                return new Archer(level);
            case MARINE:
                return new Marine(level);
            case PYRO:
                return new Pyro(level);
            case TANK:
                return new Tank(level);
            case APPRENTICE:
                return new Apprentice(level);
            case ASSASSIN:
                return new Assassin(level);
            case WICK:
                return new Wick(level);
            case MEDIC:
                return new Medic(level);
            case RAMBO:
                return new Rambo(level);
            case CHEMIST:
                return new Chemist(level);
            case UAV:
                return new Uav(level);
            default:
                return null;
        }
    }
}
