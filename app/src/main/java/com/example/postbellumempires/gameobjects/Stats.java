package com.example.postbellumempires.gameobjects;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Stats implements Serializable {

    private int health;
    private int healthBonus;
    private double attack;
    private double attackBonus;
    private double armor;
    private double armorBonus;
    private double speed;
    private double speedBonus;

    public Stats() {
    }

    public Stats(int health, double attack, double armor, double speed) {
        this.health = health;
        this.healthBonus = 0;
        this.attack = attack;
        this.attackBonus = 0;
        this.armor = armor;
        this.armorBonus = 0;
        this.speed = speed;
        this.speedBonus = 0;
    }

    @Exclude
    public int totalHealth() {
        return this.health + this.healthBonus;
    }

    @Exclude
    public double totalAttack() {
        return this.attack + this.attackBonus;
    }

    @Exclude
    public double totalArmor() {
        return this.armor + this.armorBonus;
    }

    @Exclude
    public double totalSpeed() {
        return this.speed + this.speedBonus;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealthBonus() {
        return healthBonus;
    }

    public void setHealthBonus(int healthBonus) {
        this.healthBonus = healthBonus;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getAttackBonus() {
        return attackBonus;
    }

    public void setAttackBonus(double attackBonus) {
        this.attackBonus = attackBonus;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getArmorBonus() {
        return armorBonus;
    }

    public void setArmorBonus(double armorBonus) {
        this.armorBonus = armorBonus;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeedBonus() {
        return speedBonus;
    }

    public void setSpeedBonus(double speedBonus) {
        this.speedBonus = speedBonus;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stats stats = (Stats) o;
        return health == stats.health &&
                healthBonus == stats.healthBonus &&
                Double.compare(stats.attack, attack) == 0 &&
                Double.compare(stats.attackBonus, attackBonus) == 0 &&
                Double.compare(stats.armor, armor) == 0 &&
                Double.compare(stats.armorBonus, armorBonus) == 0 &&
                Double.compare(stats.speed, speed) == 0 &&
                Double.compare(stats.speedBonus, speedBonus) == 0;
    }
}
