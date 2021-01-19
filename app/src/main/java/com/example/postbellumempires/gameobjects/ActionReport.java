package com.example.postbellumempires.gameobjects;

public class ActionReport {

    private int attacks;
    private int successfulAttacks;
    private int heals;
    private int successfulHeals;
    private int boosts;
    private int successfulBoosts;
    private int killed;

    public ActionReport() {
        this.attacks = 0;
        this.successfulAttacks = 0;
        this.heals = 0;
        this.successfulHeals = 0;
        this.boosts = 0;
        this.successfulBoosts = 0;
        this.killed = 0;
    }

    public void updateKills(int kills) {
        this.killed += kills;
    }

    public int getKills() {
        return this.killed;
    }

    public void performAttack(boolean success) {
        this.attacks++;
        if (success)
            this.successfulAttacks++;
    }

    public void performHeal(boolean success){
        this.heals++;
        if(success)
            this.successfulHeals++;
    }

    public void performBoost(boolean success){
        this.boosts++;
        if(success)
            this.successfulBoosts++;
    }

    public double attackRate() {
        if (this.attacks == 0) {
            return 0.0;
        } else {
            return (double) this.successfulAttacks / (double) this.attacks;
        }
    }

    public double healRate(){
        if(this.heals == 0){
            return 0.0;
        }else{
            return (double) this.successfulHeals / (double) this.heals;
        }
    }

    public double boostRate(){
        if(this.boosts == 0){
            return 0.0;
        }else{
            return (double) this.successfulBoosts / (double) this.boosts;
        }
    }
}
