package com.example.postbellumempires.gameobjects;

import com.google.firebase.database.Exclude;

public class PlaceBonuses {

    private int healthBonus;
    private int attackBonus;
    private int armorBonus;
    private int speedBonus;

    private int capacityBonus;

    private double resourceBonus;
    private double resourcePenalty;

    public PlaceBonuses() {
    }

    public PlaceBonuses(Place p) {
        this.healthBonus = 0;
        this.attackBonus = 0;
        this.armorBonus = 0;
        this.speedBonus = 0;
        this.capacityBonus = 0;
        this.resourceBonus = 0.0;
        this.resourcePenalty = 0.0;
    }


    public int getHealthBonus() {
        return healthBonus;
    }

    public void setHealthBonus(int healthBonus) {
        this.healthBonus = healthBonus;
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(int attackBonus) {
        this.attackBonus = attackBonus;
    }

    public int getArmorBonus() {
        return armorBonus;
    }

    public void setArmorBonus(int armorBonus) {
        this.armorBonus = armorBonus;
    }

    public int getSpeedBonus() {
        return speedBonus;
    }

    public void setSpeedBonus(int speedBonus) {
        this.speedBonus = speedBonus;
    }

    public int getCapacityBonus() {
        return capacityBonus;
    }

    public void setCapacityBonus(int capacityBonus) {
        this.capacityBonus = capacityBonus;
    }

    public double getResourceBonus() {
        return resourceBonus;
    }

    public void setResourceBonus(double resourceBonus) {
        this.resourceBonus = resourceBonus;
    }

    public double getResourcePenalty() {
        return resourcePenalty;
    }

    public void setResourcePenalty(double resourcePenalty) {
        this.resourcePenalty = resourcePenalty;
    }

    @Exclude
    public void reset() {
        this.healthBonus = 0;
        this.attackBonus = 0;
        this.armorBonus = 0;
        this.speedBonus = 0;
        this.capacityBonus = 0;
        this.resourceBonus = 0.0;
        this.resourcePenalty = 0.0;
    }

    @Exclude
    public void addHealth(int health) {
        this.healthBonus += health;
    }

    @Exclude
    public void addAttack(int attack) {
        this.attackBonus += attack;
    }

    @Exclude
    public void addArmor(int armor) {
        this.armorBonus += armor;
    }

    @Exclude
    public void addSpeed(int speed) {
        this.speedBonus += speed;
    }

    @Exclude
    public void addCapacity(int capacity) {
        this.capacityBonus += capacity;
    }

    @Exclude
    public void addResourceBonus(double resourceBonus) {
        this.resourceBonus += resourceBonus;
    }

    @Exclude
    public void addResourcePenalty(double resourcePenalty) {
        this.resourcePenalty += resourcePenalty;
    }
}
