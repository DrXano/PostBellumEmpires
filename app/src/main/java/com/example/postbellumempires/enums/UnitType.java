package com.example.postbellumempires.enums;

import com.example.postbellumempires.gameobjects.ActionReport;
import com.example.postbellumempires.gameobjects.BattleUnit;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;

import java.util.Map;
import java.util.Random;

public enum UnitType {
    MARINE("Marine") {
        @Override
        public void act(BattleUnit bu, BattleUnit[] friendly, BattleUnit[] hostile, ActionReport report, Map<String, Integer> friendlyCount, Map<String, Integer> hostileCount, Player player, Place place) {
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead())
                selected = new Random().nextInt(hostile.length);

            report.performattack(hostile[selected].damage(bu.getAttack()));
            if (hostile[selected].isDead()) {
                report.updateKills(1);
                remove(hostile[selected], player, place);

                int count = hostileCount.get(hostile[selected].getType().name) - 1;
                if (count <= 0) {
                    hostileCount.remove(hostile[selected].getType().name);
                } else {
                    hostileCount.put(hostile[selected].getType().name, count);
                }
            }
        }
    },
    PYRO("Pyro") {
        @Override
        public void act(BattleUnit bu, BattleUnit[] friendly, BattleUnit[] hostile, ActionReport report, Map<String, Integer> friendlyCount, Map<String, Integer> hostileCount, Player player, Place place) {
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead())
                selected = new Random().nextInt(hostile.length);

            report.performattack(hostile[selected].damage(bu.getAttack()));
            if (hostile[selected].isDead()) {
                report.updateKills(1);
                remove(hostile[selected], player, place);

                int count = hostileCount.get(hostile[selected].getType().name) - 1;
                if (count <= 0) {
                    hostileCount.remove(hostile[selected].getType().name);
                } else {
                    hostileCount.put(hostile[selected].getType().name, count);
                }
            }
        }
    },
    TANK("Tank") {
        @Override
        public void act(BattleUnit bu, BattleUnit[] friendly, BattleUnit[] hostile, ActionReport report, Map<String, Integer> friendlyCount, Map<String, Integer> hostileCount, Player player, Place place) {
            int selected = new Random().nextInt(hostile.length);
            int kills = 0;
            while (hostile[selected].isDead())
                selected = new Random().nextInt(hostile.length);

            report.performattack(hostile[selected].damage(bu.getAttack()));
            if (hostile[selected].isDead()) {
                kills++;
                remove(hostile[selected], player, place);

                int count = hostileCount.get(hostile[selected].getType().name) - 1;
                if (count <= 0) {
                    hostileCount.remove(hostile[selected].getType().name);
                } else {
                    hostileCount.put(hostile[selected].getType().name, count);
                }
            }

            int left = selected - 1;
            int right = selected + 1;

            if (left > 0 && !hostile[left].isDead()) {
                hostile[left].damage(bu.getAttack() / 2);
                if (hostile[left].isDead()) {
                    kills++;
                    remove(hostile[left], player, place);

                    int count = hostileCount.get(hostile[left].getType().name) - 1;
                    if (count <= 0) {
                        hostileCount.remove(hostile[left].getType().name);
                    } else {
                        hostileCount.put(hostile[left].getType().name, count);
                    }
                }
            }

            if (right < hostile.length && !hostile[right].isDead()) {
                hostile[right].damage(bu.getAttack() / 2);
                if (hostile[right].isDead()) {
                    kills++;
                    remove(hostile[right], player, place);

                    int count = hostileCount.get(hostile[right].getType().name) - 1;
                    if (count <= 0) {
                        hostileCount.remove(hostile[right].getType().name);
                    } else {
                        hostileCount.put(hostile[right].getType().name, count);
                    }
                }
            }
            report.updateKills(kills);
        }
    };

    public final String name;

    UnitType(String name) {
        this.name = name;
    }

    public abstract void act(BattleUnit bu, BattleUnit[] friendly, BattleUnit[] hostile, ActionReport report, Map<String, Integer> friendlyCount, Map<String, Integer> hostileCount, Player player, Place place);

    void remove(BattleUnit bu, Player player, Place place) {
        if (player != null) {
            player.remove(bu);
        }

        if (place != null) {
            place.remove(bu);
        }
    }
}
