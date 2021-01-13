package com.example.postbellumempires.gameobjects;

public class ActionReport {

    private int actions;
    private int successful;
    private int killed;

    public ActionReport() {
        this.actions = 0;
        this.successful = 0;
        this.killed = 0;
    }

    public void updateKills(int kills) {
        this.killed += kills;
    }

    public int getKills() {
        return this.killed;
    }

    public void performattack(boolean success) {
        this.actions++;
        if (success)
            this.successful++;
    }

    public double successRate() {
        if (this.actions == 0) {
            return 0.0;
        } else {
            return (double) this.successful / (double) this.actions;
        }
    }
}
