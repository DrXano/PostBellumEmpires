package com.example.postbellumempires.gameobjects;

import com.example.postbellumempires.enums.ExpReward;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Battle implements Serializable {

    private GameUnit[] playerArmy;
    private BattleUnit[] myArmy;
    private BattleUnit[] enemyArmy;
    private PlaceArmy toRemove;
    private Player player;
    private Place place;
    private boolean inBattle;
    private boolean victory;
    private int killed;

    public Battle(Player player, Place place, GameUnit[] playerArmy) {
        this.player = player;
        this.place = place;
        this.playerArmy = playerArmy;
        this.inBattle = true;
        this.victory = false;
        this.myArmy = processUnits(playerArmy);
        List<GameUnit> enemyUnits = new ArrayList<>(place.getArmy().getUnits().values());
        GameUnit[] enemyArr = new GameUnit[enemyUnits.size()];
        enemyUnits.toArray(enemyArr);
        this.enemyArmy = processUnits(enemyArr);
        this.toRemove = new PlaceArmy(100);
        this.killed = 0;
    }

    public void start() {
        while (this.inBattle){
            for(BattleUnit bu : this.myArmy){
                if(!bu.isDead()){

                }
            }

            for(BattleUnit bu : this.enemyArmy){
                if(!bu.isDead()){

                }
            }

            boolean myArmyDead = isDefeated(this.myArmy);
            boolean enemyArmyDead = isDefeated(this.enemyArmy);
            if(myArmyDead || enemyArmyDead){
                this.inBattle = false;
                if(enemyArmyDead){
                    this.victory = true;
                }
            }
        }

        int exp = killed * ExpReward.UNIT_KILLED.reward;
        if (victory) {
            exp += ExpReward.VICTORY.reward;
            place.free();
        }else{
            List<GameUnit> toRemoveList = new ArrayList<>(this.toRemove.getUnits().values());
            GameUnit[] toRemoveArr = new GameUnit[toRemoveList.size()];
            toRemoveList.toArray(toRemoveArr);
            place.removeUnits(toRemoveArr);
        }

        player.removeUnits(playerArmy);
        player.giveExp(exp);
        player.updatePlayer();
    }

    private BattleUnit[] processUnits(GameUnit[] units){
        List<BattleUnit> processed = new ArrayList<>();
        for(GameUnit gu : units) {
            int quantity = gu.getQuantity();
            for(int i = 0; i < quantity; i++){
                processed.add(gu.toBattleUnit());
            }
        }
        BattleUnit[] arr = new BattleUnit[processed.size()];
        processed.toArray(arr);
        return arr;
    }

    private boolean isDefeated(BattleUnit[] army){
        for(BattleUnit bu : army){
            if(!bu.isDead()){
                return false;
            }
        }
        return true;
    }
}
