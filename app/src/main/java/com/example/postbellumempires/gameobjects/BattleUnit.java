package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.UnitType;

public class BattleUnit {

    private final int level;
    private final String name;
    private final int health;
    private final int size;
    private int currentHealth;
    private double attack;
    private final double armor;
    private final double speed;
    private final UnitType type;

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
        return this.currentHealth == 0;
    }

    public boolean isFullyHealed() {
        return this.health == this.currentHealth;
    }

    public boolean inDanger() {
        double healthRatio = (double) this.currentHealth / (double) this.health;
        return healthRatio <= 0.1;
    }

    public boolean heal(double health) {
        if(this.health == this.currentHealth){
            return false;
        }else{
            this.currentHealth += health;
            if (this.currentHealth > this.health)
                this.currentHealth = this.health;
            return true;
        }
    }

    public boolean damage(double damage) {
        if (damage <= this.armor) {
            return false;
        } else {
            this.currentHealth -= (damage - this.armor);
            if (this.currentHealth < 0)
                this.currentHealth = 0;
            return true;
        }
    }

    public boolean boostAttack(double bonus){
        double maxAttack = this.attack * 1.5;
        if(this.attack >= maxAttack){
            return false;
        }else {
            this.attack += bonus;
            if(this.attack >= maxAttack)
                this.attack = maxAttack;
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
