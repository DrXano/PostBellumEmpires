package com.example.postbellumempires.gameobjects;

import android.content.res.Resources;

import com.example.postbellumempires.BattleActivity;
import com.example.postbellumempires.R;
import com.example.postbellumempires.enums.ExpReward;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Battle implements Serializable {

    private static final int NUMBER_OF_ROUNDS = 60;
    private static final int INTERVAL = 1000;

    private BattleUnit[] myArmy;
    private BattleUnit[] enemyArmy;

    private Map<String, Integer> myArmyCount;
    private Map<String, Integer> enemyArmyCount;

    private final Player player;
    private final Place place;
    private boolean inBattle;
    private boolean victory;
    private int friendlykilled;
    private int enemykilled;

    private Resources resources;

    private BattleActivity parent;

    public Battle(Player player, Place place, GameUnit[] playerArmy) {
        this.player = player;
        this.place = place;
        this.inBattle = true;
        this.victory = false;
        this.myArmyCount = new HashMap<>();
        this.enemyArmyCount = new HashMap<>();
        this.myArmy = processUnits(playerArmy, myArmyCount);
        List<GameUnit> enemyUnits = new ArrayList<>(place.getArmy().getUnits().values());
        GameUnit[] enemyArr = new GameUnit[enemyUnits.size()];
        enemyUnits.toArray(enemyArr);
        this.enemyArmy = processUnits(enemyArr, enemyArmyCount);
        this.friendlykilled = 0;
        this.enemykilled = 0;
    }

    public void setActivity(BattleActivity parent, Resources resources){
        this.parent = parent;
        this.resources = resources;
        this.parent.UpdateUI(myArmyCount,enemyArmyCount,myArmy.length,enemyArmy.length);
    }

    public void start() {
        Thread battle = new Thread(new Runnable() {
            @Override
            public void run() {
                parent.addMessage(new BattleMessage("The Battle has begun!",resources.getColor(R.color.neutral)));
                for(int i = 1; i <= NUMBER_OF_ROUNDS && inBattle; i++) {

                    ActionReport myReport = new ActionReport();
                    ActionReport enemyReport = new ActionReport();

                    if (!isDefeated(myArmy) && !isDefeated(enemyArmy)) {
                        for (BattleUnit bu : myArmy) {
                            if (!bu.isDead()) {
                                bu.getType().act(bu, myArmy, enemyArmy, myReport, myArmyCount, enemyArmyCount,null,place);
                            }
                        }
                        if(myReport.getKills() > 0) {
                            enemykilled += myReport.getKills();
                            parent.updateEnemyArmy(enemyArmyCount,enemyArmy.length-enemykilled);
                        }

                        parent.addMessage(getMessageForPlayer(myReport));
                    }

                    if (!isDefeated(myArmy) && !isDefeated(enemyArmy)) {
                        for (BattleUnit bu : enemyArmy) {
                            if (!bu.isDead()) {
                                bu.getType().act(bu, enemyArmy, myArmy, enemyReport, enemyArmyCount, myArmyCount,player,null);
                            }
                        }
                        if(enemyReport.getKills() > 0) {
                            friendlykilled += enemyReport.getKills();
                            parent.updateMyArmy(myArmyCount,myArmy.length-friendlykilled);
                        }

                        parent.addMessage(getMessageForEnemy(enemyReport));
                    }

                    boolean myArmyDead = isDefeated(myArmy);
                    boolean enemyArmyDead = isDefeated(enemyArmy);
                    if (myArmyDead || enemyArmyDead) {
                        inBattle = false;
                        if (enemyArmyDead) {
                            victory = true;
                        }
                    }
                }
            }
        });

        battle.start();

        try {
            battle.join();

            parent.addMessage(new BattleMessage("The Battle has ended!",resources.getColor(R.color.neutral)));

            int exp = enemykilled * ExpReward.UNIT_KILLED.reward;
            if (victory) {
                parent.addMessage(new BattleMessage("Victory!",resources.getColor(player.getPlayerFaction().primaryColor)));
                exp += ExpReward.VICTORY.reward;
                place.free();
            }else{
                parent.addMessage(new BattleMessage("Defeat!",resources.getColor(place.getFaction().primaryColor)));
                place.updatePlace();
            }

            player.giveExp(exp);
            player.updatePlayer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BattleMessage getMessageForPlayer(ActionReport myReport) {
        int color = resources.getColor(player.getPlayerFaction().primaryColor);
        String message = "Your army has engaged the enemy";
        return new BattleMessage(message,color);
    }

    private BattleMessage getMessageForEnemy(ActionReport enemyReport) {
        int color = resources.getColor(place.getFaction().primaryColor);
        String message = "The enemy has engaged you";
        return new BattleMessage(message,color);
    }

    private BattleUnit[] processUnits(GameUnit[] units, Map<String, Integer> armyCount) {
        List<BattleUnit> processed = new ArrayList<>();
        for (GameUnit gu : units) {
            int quantity = gu.getQuantity();
            for (int i = 0; i < quantity; i++) {
                processed.add(gu.toBattleUnit());

                if (armyCount.containsKey(gu.getEType().name)) {
                    int count = armyCount.get(gu.getEType().name);
                    armyCount.put(gu.getEType().name, count + 1);
                } else {
                    armyCount.put(gu.getEType().name, 1);
                }
            }
        }
        BattleUnit[] arr = new BattleUnit[processed.size()];
        processed.toArray(arr);
        return arr;
    }

    private boolean isDefeated(BattleUnit[] army) {
        for (BattleUnit bu : army) {
            if (!bu.isDead()) {
                return false;
            }
        }
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public Place getPlace() {
        return place;
    }
}
