package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;

public class BattleUnit {

    private int level;
    private String name;
    private int health;
    private int size;
    private int currentHealth;
    private double attack;
    private double armor;
    private double speed;
    private UnitType type;

    public BattleUnit(GameUnit unit) {
        this.level = unit.getLevel();
        this.name = unit.getName();
        this.type = unit.getEType();
        this.size = unit.getSize();
        this.health = unit.getStats().totalHealth();
        this.currentHealth = unit.getStats().totalHealth();
        this.attack = unit.getStats().totalAttack();
        this.armor = unit.getStats().totalArmor();
        this.speed = unit.getStats().totalSpeed();
    }

    public boolean isDead() {
        return this.currentHealth <= 0;
    }

    public boolean isFullyHealed() {
        return this.health == this.currentHealth;
    }

    public boolean inDanger() {
        double healthRatio = (double) this.currentHealth / (double) this.health;
        return healthRatio <= 0.1;
    }

    public void heal(int health) {
        this.currentHealth += health;
        if (this.currentHealth > this.health)
            this.currentHealth = this.health;
    }

    public boolean damage(double damage) {
        if (damage <= this.armor) {
            return false;
        } else {
            this.currentHealth -= this.armor - damage;
            if (this.currentHealth < 0)
                this.currentHealth = 0;
            return true;
        }
    }

    @Override
    public String toString() {
        return "Level" + this.level + "_" + this.name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public double getAttack() {
        return attack;
    }

    public double getArmor() {
        return armor;
    }

    public double getSpeed() {
        return speed;
    }

    public UnitType getType() {
        return type;
    }

    public GameUnit toGameUnit() {
        return new GameUnit(this.level, this.name, this.size, 1, this.type, null);
    }
}
