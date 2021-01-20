package com.example.postbellumempires.enums;

import androidx.annotation.NonNull;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.ActionReport;
import com.example.postbellumempires.gameobjects.BattleUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;

import java.util.Map;
import java.util.Random;

public enum UnitType {
    ARCHER("Archer", R.string.archerDesc, 1, new Item[]{new Item(GameResource.FOOD, 10), new Item(GameResource.WOOD, 10), new Item(GameResource.STONE, 5)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed(); i++) {
                    if (!hostile[selected].isDead()) {
                        report.performAttack(hostile[selected].damage(bu.getAttack()));
                    }
                }

                if (hostile[selected].isDead()) {
                    report.updateKills(1);
                    remove(hostile[selected], player, place);

                    int count;
                    if (hostileCount.containsKey(hostile[selected].getType().name)) {
                        count = hostileCount.get(hostile[selected].getType().name) - 1;
                    } else {
                        count = 0;
                    }
                    if (count <= 0) {
                        hostileCount.remove(hostile[selected].getType().name);
                    } else {
                        hostileCount.put(hostile[selected].getType().name, count);
                    }
                }
            }
        }
    },
    MARINE("Marine", R.string.marineDesc, 1, new Item[]{new Item(GameResource.FOOD, 15), new Item(GameResource.WOOD, 10)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed(); i++) {
                    if (!hostile[selected].isDead()) {
                        report.performAttack(hostile[selected].damage(bu.getAttack()));
                    }
                }

                if (hostile[selected].isDead()) {
                    report.updateKills(1);
                    remove(hostile[selected], player, place);

                    int count;
                    if (hostileCount.containsKey(hostile[selected].getType().name)) {
                        count = hostileCount.get(hostile[selected].getType().name) - 1;
                    } else {
                        count = 0;
                    }
                    if (count <= 0) {
                        hostileCount.remove(hostile[selected].getType().name);
                    } else {
                        hostileCount.put(hostile[selected].getType().name, count);
                    }
                }
            }
        }
    },
    PYRO("Pyro", R.string.pyroDesc, 3, new Item[]{new Item(GameResource.FOOD, 15), new Item(GameResource.WOOD, 25)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            for (int i = 1; i <= bu.getSpeed(); i++) {
                int number_of_searches = 1;
                int selected = new Random().nextInt(hostile.length);
                while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                    selected = new Random().nextInt(hostile.length);
                    number_of_searches++;
                }

                if (!hostile[selected].isDead()) {
                    report.performAttack(hostile[selected].damage(bu.getAttack()));
                    if (hostile[selected].isDead()) {
                        report.updateKills(1);
                        remove(hostile[selected], player, place);

                        int count;
                        if (hostileCount.containsKey(hostile[selected].getType().name)) {
                            count = hostileCount.get(hostile[selected].getType().name) - 1;
                        } else {
                            count = 0;
                        }
                        if (count <= 0) {
                            hostileCount.remove(hostile[selected].getType().name);
                        } else {
                            hostileCount.put(hostile[selected].getType().name, count);
                        }
                    }
                }
            }
        }
    },
    TANK("Tank", R.string.tankDesc, 7, new Item[]{new Item(GameResource.FOOD, 25), new Item(GameResource.IRON, 15)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int kills = 0;
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed() && !hostile[selected].isDead(); i++) {
                    report.performAttack(hostile[selected].damage(bu.getAttack()));
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
                }
                report.updateKills(kills);
            }
        }
    },
    APPRENTICE("Apprentice", R.string.apprenticeDesc, 3, new Item[]{new Item(GameResource.FOOD, 10), new Item(GameResource.WOOD, 10), new Item(GameResource.STONE, 10)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed(); i++) {
                    if (!hostile[selected].isDead()) {
                        report.performAttack(hostile[selected].damage(bu.getAttack()));
                    }
                }

                if (hostile[selected].isDead()) {
                    report.updateKills(1);
                    remove(hostile[selected], player, place);

                    int count;
                    if (hostileCount.containsKey(hostile[selected].getType().name)) {
                        count = hostileCount.get(hostile[selected].getType().name) - 1;
                    } else {
                        count = 0;
                    }
                    if (count <= 0) {
                        hostileCount.remove(hostile[selected].getType().name);
                    } else {
                        hostileCount.put(hostile[selected].getType().name, count);
                    }
                }
            }
        }
    },
    ASSASSIN("Assassin", R.string.assassinDesc, 1, new Item[]{new Item(GameResource.FOOD, 10), new Item(GameResource.WOOD, 40)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed(); i++) {
                    if (!hostile[selected].isDead()) {
                        report.performAttack(hostile[selected].damage(bu.getAttack()));
                    }
                }

                if (hostile[selected].isDead()) {
                    report.updateKills(1);
                    remove(hostile[selected], player, place);

                    int count;
                    if (hostileCount.containsKey(hostile[selected].getType().name)) {
                        count = hostileCount.get(hostile[selected].getType().name) - 1;
                    } else {
                        count = 0;
                    }
                    if (count <= 0) {
                        hostileCount.remove(hostile[selected].getType().name);
                    } else {
                        hostileCount.put(hostile[selected].getType().name, count);
                    }
                }
            }
        }
    },
    WICK("Wick", R.string.wickDesc, 3, new Item[]{new Item(GameResource.FOOD, 20), new Item(GameResource.URANIUM, 3)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed(); i++) {
                    if (!hostile[selected].isDead()) {
                        report.performAttack(hostile[selected].damage(bu.getAttack()));
                    }
                }

                if (hostile[selected].isDead()) {
                    report.updateKills(1);
                    remove(hostile[selected], player, place);

                    int count;
                    if (hostileCount.containsKey(hostile[selected].getType().name)) {
                        count = hostileCount.get(hostile[selected].getType().name) - 1;
                    } else {
                        count = 0;
                    }
                    if (count <= 0) {
                        hostileCount.remove(hostile[selected].getType().name);
                    } else {
                        hostileCount.put(hostile[selected].getType().name, count);
                    }
                }
            }
        }
    },
    MEDIC("Medic", R.string.medicDesc, 3, new Item[]{new Item(GameResource.FOOD, 20), new Item(GameResource.MEDICINE, 10), new Item(GameResource.KNOWLEDGE, 10)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            for (int i = 1; i <= bu.getSpeed(); i++) {
                int number_of_searches = 1;
                int selected = new Random().nextInt(friendly.length);
                while (friendly[selected].isDead() && number_of_searches <= friendly.length) {
                    selected = (selected + 1) % friendly.length;
                    number_of_searches++;
                }

                if (!friendly[selected].isDead()) {
                    report.performHeal(friendly[selected].heal(bu.getAttack()));
                }
            }
        }
    },
    RAMBO("Rambo", R.string.ramboDesc, 5, new Item[]{new Item(GameResource.FOOD, 25), new Item(GameResource.IRON, 20), new Item(GameResource.URANIUM, 4)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            for (int i = 1; i <= bu.getSpeed(); i++) {
                int number_of_searches = 1;
                int selected = new Random().nextInt(hostile.length);
                while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                    selected = new Random().nextInt(hostile.length);
                    number_of_searches++;
                }

                if (!hostile[selected].isDead()) {
                    report.performAttack(hostile[selected].damage(bu.getAttack()));
                    if (hostile[selected].isDead()) {
                        report.updateKills(1);
                        remove(hostile[selected], player, place);

                        int count;
                        if (hostileCount.containsKey(hostile[selected].getType().name)) {
                            count = hostileCount.get(hostile[selected].getType().name) - 1;
                        } else {
                            count = 0;
                        }
                        if (count <= 0) {
                            hostileCount.remove(hostile[selected].getType().name);
                        } else {
                            hostileCount.put(hostile[selected].getType().name, count);
                        }
                    }
                }
            }
        }
    },
    CHEMIST("Chemist", R.string.chemistDesc, 7, new Item[]{new Item(GameResource.FOOD, 20), new Item(GameResource.IRON, 15), new Item(GameResource.KNOWLEDGE, 15)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            int kills = 0;
            int number_of_searches = 1;
            int selected = new Random().nextInt(hostile.length);
            while (hostile[selected].isDead() && number_of_searches <= hostile.length) {
                selected = new Random().nextInt(hostile.length);
                number_of_searches++;
            }

            if (!hostile[selected].isDead()) {
                for (int i = 1; i <= bu.getSpeed() && !hostile[selected].isDead(); i++) {
                    report.performAttack(hostile[selected].damage(bu.getAttack()));
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
                }
                report.updateKills(kills);
            }
        }
    },
    UAV("UAV", R.string.uavDesc, 15, new Item[]{new Item(GameResource.IRON, 50), new Item(GameResource.URANIUM, 10), new Item(GameResource.WOOD, 45)}) {
        @Override
        public void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place) {
            for (int i = 1; i <= friendly.length; i++) {
                int number_of_searches = 1;
                int selected = new Random().nextInt(friendly.length);
                while (friendly[selected].isDead() && number_of_searches <= friendly.length) {
                    selected = (selected + 1) % friendly.length;
                    number_of_searches++;
                }

                if (!friendly[selected].isDead()) {
                    report.performBoost(friendly[selected].boostAttack(bu.getAttack()));
                }
            }
        }
    };

    public final String name;
    public final int description;
    public final int size;
    public final Item[] trainCost;

    UnitType(String name, int description, int size, Item[] trainCost) {
        this.name = name;
        this.description = description;
        this.size = size;
        this.trainCost = trainCost;
    }

    public abstract void act(@NonNull BattleUnit bu, @NonNull BattleUnit[] friendly, @NonNull BattleUnit[] hostile, @NonNull ActionReport report, @NonNull Map<String, Integer> friendlyCount, @NonNull Map<String, Integer> hostileCount, Player player, Place place);

    void remove(BattleUnit bu, Player player, Place place) {
        if (player != null) {
            player.remove(bu);
        }

        if (place != null) {
            place.remove(bu);
        }
    }
}
