package com.example.postbellumempires.enums;

import java.util.HashMap;
import java.util.Map;

public enum PlayerLevel {
    Level1(1, 1000, 30, UnitType.MARINE),
    Level2(2, 2000, 33, UnitType.PYRO),
    Level3(3, 4000, 36, null),
    Level4(4, 8000, 39, null),
    Level5(5, 16000, 42, null),
    Level6(6, 32000, 45, null),
    Level7(7, 64000, 48, UnitType.TANK),
    Level8(8, 128000, 51, null),
    Level9(9, 256000, 54, null),
    Level10(10, 512000, 57, null),
    Level11(11, 1024000, 60, null);

    private static final Map<Integer, PlayerLevel> BY_LEVEL = new HashMap<>();
    private static final Map<UnitType, PlayerLevel> BY_UNIT_REWARD = new HashMap<>();

    static {
        for (PlayerLevel l : values()) {
            BY_LEVEL.put(l.level, l);
            if (l.unitReward != null)
                BY_UNIT_REWARD.put(l.unitReward, l);
        }
    }

    public final int level;
    public final int maxExp;
    public final int maxCapacity;
    public final UnitType unitReward;

    PlayerLevel(int level, int maxExp, int maxCapacity, UnitType unitReward) {
        this.level = level;
        this.maxExp = maxExp;
        this.maxCapacity = maxCapacity;
        this.unitReward = unitReward;
    }

    public static PlayerLevel valueOfLevel(int level) {
        return BY_LEVEL.get(level);
    }

    public static PlayerLevel valueOfUnit(UnitType type) {
        return BY_UNIT_REWARD.get(type);
    }

    public static int getMaxLevel() {
        return values().length;
    }

    public static boolean isMaxed(int level) {
        return level == getMaxLevel();
    }
}
