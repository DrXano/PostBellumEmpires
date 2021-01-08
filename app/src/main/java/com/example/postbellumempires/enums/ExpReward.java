package com.example.postbellumempires.enums;

public enum ExpReward {
    POST_CLAIMED(500),
    VICTORY(1000),
    STRUCTURE_BUILDED(100),
    UNIT_KILLED(20),
    UNIT_DEPLOYED(10);

    public final int reward;

    ExpReward(int reward) {
        this.reward = reward;
    }
}
