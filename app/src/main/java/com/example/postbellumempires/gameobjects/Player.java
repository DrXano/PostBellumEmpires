package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.Faction;
import com.google.firebase.database.Exclude;

public class Player {

    private String email;
    private String inGameName;
    private int level;
    private int exp;
    private int maxExp;
    private Faction playerFaction;

    public Player() {}

    public Player(String email, String inGameName, Faction faction, int level, int exp, int maxExp) {
        this.email = email;
        this.inGameName = inGameName;
        this.playerFaction = faction;
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInGameName() {
        return inGameName;
    }

    public void setInGameName(String inGameName) {
        this.inGameName = inGameName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getMaxExp() {
        return maxExp;
    }

    public void setMaxExp(int maxExp) {
        this.maxExp = maxExp;
    }

    public String getPFaction() {
        if(playerFaction == null){
            return null;
        }else{
            return playerFaction.name;
        }
    }

    public void setPFaction(String playerFaction) {
        if(playerFaction == null){
            this.playerFaction = null;
        }else{
            this.playerFaction = Faction.valueOfName(playerFaction);
        }
    }

    @Exclude
    public Faction getPlayerFaction() {
        return playerFaction;
    }

    @Exclude
    public void setPlayerFaction(Faction playerFaction) {
        this.playerFaction = playerFaction;
    }
}
