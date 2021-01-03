package com.example.postbellumempires.gameobjects.units;

import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.enums.UnitType;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Stats;
import com.example.postbellumempires.gameobjects.Unit;

import java.util.HashMap;
import java.util.Map;

public class Tank extends Unit {
    private static final String name = "Tank";
    private static final int size = 10;
    private static Item[] trainCost = new Item[]{new Item(GameResource.FOOD, 300), new Item(GameResource.IRON, 500)};
    private static UnitType type = UnitType.TANK;

    private Level levelInfo;
    private Stats stats;

    public Tank() {
        super(name, size, trainCost, type);
        this.levelInfo = Level.Level1;
        this.stats = this.levelInfo.stats;
    }

    public Tank(int level) {
        super(name, size, trainCost, type);
        int lev = (level > getMaxLevel()) ? getMaxLevel() : level;
        this.levelInfo = Level.getByLevel(lev);
        this.stats = this.levelInfo.stats;
    }

    @Override
    public Stats getStats() {
        return this.stats;
    }

    @Override
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public int getLevelInfo() {
        return levelInfo.level;
    }

    @Override
    public int getMaxLevel() {
        return Level.values().length;
    }

    @Override
    public boolean isMaxed() {
        return this.levelInfo.level == this.getMaxLevel();
    }

    @Override
    public Item[] getUpgradeCost() {
        return this.levelInfo.upgradeCost;
    }

    @Override
    public void levelUp() {
        if (!this.isMaxed()) {
            Level newLevel = Level.getByLevel(this.levelInfo.level + 1);
            this.stats.setHealth(newLevel.stats.getHealth());
            this.stats.setAttack(newLevel.stats.getAttack());
            this.stats.setArmor(newLevel.stats.getArmor());
            this.stats.setSpeed(newLevel.stats.getSpeed());
            this.levelInfo = newLevel;
        }
    }

    @Override
    public GameUnit toGameUnit() {
        return new GameUnit(this.levelInfo.level, super.getName(), super.getSize(), super.getType(), this.stats);
    }

    private enum Level {
        Level1(1, new Stats(500, 50, 100, 0.5), null),
        Level2(2, new Stats(600, 100, 200, 0.5), new Item[]{new Item(GameResource.KNOWLEDGE, 20), new Item(GameResource.FOOD, 500), new Item(GameResource.IRON, 500)}),
        Level3(3, new Stats(700, 150, 300, 0.5), new Item[]{new Item(GameResource.KNOWLEDGE, 30), new Item(GameResource.FOOD, 1000), new Item(GameResource.IRON, 1000)});

        private static final Map<Integer, Level> BY_LEVEL = new HashMap<>();

        static {
            for (Level l : values()) {
                BY_LEVEL.put(l.level, l);
            }
        }

        public final int level;
        public final Stats stats;
        public final Item[] upgradeCost;

        Level(int level, Stats stats, Item[] upgradeCost) {
            this.level = level;
            this.stats = stats;
            this.upgradeCost = upgradeCost;
        }

        public static Level getByLevel(int level) {
            return BY_LEVEL.get(level);
        }
    }
}