package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.enums.UnitType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Player {

    private String email;
    private String inGameName;
    private int level;
    private int exp;
    private int maxExp;
    private Faction playerFaction;
    private Inventory inv;
    private PlayerArmy army;

    public Player() {
    }

    public Player(String email, String inGameName, Faction faction, int level, int exp, int maxExp) {
        this.email = email;
        this.inGameName = inGameName;
        this.playerFaction = faction;
        this.level = level;
        this.exp = exp;
        this.maxExp = maxExp;
        this.inv = new Inventory(this.inGameName);
        this.army = new PlayerArmy(30);
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
        if (playerFaction == null) {
            return null;
        } else {
            return playerFaction.name;
        }
    }

    public void setPFaction(String playerFaction) {
        if (playerFaction == null) {
            this.playerFaction = null;
        } else {
            this.playerFaction = Faction.valueOfName(playerFaction);
        }
    }

    public PlayerArmy getArmy() {
        return army;
    }

    public void setArmy(PlayerArmy army) {
        this.army = army;
    }

    @Exclude
    public Faction getPlayerFaction() {
        return playerFaction;
    }

    @Exclude
    public void setPlayerFaction(Faction playerFaction) {
        this.playerFaction = playerFaction;
    }

    public Inventory getInv() {
        return inv;
    }

    public void setInv(Inventory inv) {
        this.inv = inv;
    }

    @Exclude
    public void addItem(GameResource resource, int quantity) {
        this.inv.addItem(resource, quantity);
    }

    @Exclude
    public void removeItem(GameResource resource, int quantity) {
        this.inv.removeItem(resource, quantity);
    }

    @Exclude
    public boolean hasItem(GameResource resource) {
        return this.inv.hasItem(resource);
    }

    @Exclude
    public void updatePlayer() {
        DatabaseReference playerRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        playerRef.setValue(this);
    }

    @Exclude
    public void emptyArmy(){
        if(army != null){
            this.army.emptyArmy();
        }else {
            this.army = new PlayerArmy(30);
        }
        this.updatePlayer();
    }

    @Exclude
    public boolean addUnit(UnitType type, int quantity){
        boolean result = this.army.add(type,quantity);
        if(result)
            this.updatePlayer();
        return result;
    }

    @Exclude
    public boolean removeUnit(UnitType type, int quantity){
        boolean result = this.army.remove(type,quantity);
        if(result)
            this.updatePlayer();
        return result;
    }

    @Exclude
    public void levelUpUnit(UnitType type){
        this.army.levelUp(type);
        this.updatePlayer();
    }

    @Exclude
    public void unlockUnit(UnitType type){
        this.army.unlock(type);
        this.updatePlayer();
    }
}
