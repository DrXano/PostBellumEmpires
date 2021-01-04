package com.example.postbellumempires.gameobjects.units;

import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.enums.UnitType;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Stats;
import com.example.postbellumempires.gameobjects.Unit;

import java.util.HashMap;
import java.util.Map;

public class Marine extends Unit {
    private static final String name = "Marine";
    private static final int size = 1;
    private static final Item[] trainCost = new Item[]{new Item(GameResource.FOOD, 50), new Item(GameResource.WOOD, 100)};
    private static final UnitType type = UnitType.MARINE;

    private Level levelInfo;
    private Stats stats;

    public Marine() {
        super(name, size, trainCost, type);
        this.levelInfo = Level.Level0;
        this.stats = this.levelInfo.stats;
    }

    public Marine(int level) {
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
    public int getLevel() {
        return levelInfo.level;
    }

    @Override
    public int getMaxLevel() {
        return Level.values().length-1;
    }

    @Override
    public boolean isMaxed() {
        return this.levelInfo.level == this.getMaxLevel();
    }

    @Override
    public Item[] getUpgradeCost() {
        Level lvl = Level.getByLevel(this.levelInfo.level + 1);
        if(lvl == null)
            return new Item[0];
        return lvl.upgradeCost;
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
    public String toString() {
        return "Level" + this.levelInfo.level + "_" + this.getName();
    }

    @Override
    public GameUnit toGameUnit() {
        return new GameUnit(this.levelInfo.level, super.getName(), super.getSize(), super.getType(), this.stats);
    }

    private enum Level {
        Level0(0, new Stats(0, 0, 0, 0), null),
        Level1(1, new Stats(150, 10, 10, 1), null),
        Level2(2, new Stats(160, 15, 20, 1), new Item[]{new Item(GameResource.KNOWLEDGE, 20), new Item(GameResource.FOOD, 200)}),
        Level3(3, new Stats(170, 20, 30, 1), new Item[]{new Item(GameResource.KNOWLEDGE, 30), new Item(GameResource.FOOD, 300)});

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
