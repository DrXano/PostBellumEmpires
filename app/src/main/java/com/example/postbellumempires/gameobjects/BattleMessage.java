package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.Faction;

public class BattleMessage {

    private final String message;
    private final Faction faction;

    public BattleMessage(String message, Faction faction) {
        this.message = message;
        this.faction = faction;
    }

    public String getMessage() {
        return message;
    }

    public Faction getFaction() {
        return faction;
    }
}
