package com.example.postbellumempires.gameobjects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;

import com.example.postbellumempires.BattleActivity;
import com.example.postbellumempires.R;
import com.example.postbellumempires.dialogs.EndOfBattleDialog;
import com.example.postbellumempires.enums.ExpReward;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Battle implements Serializable {

    private static final int NUMBER_OF_ROUNDS = 60;

    private static final int INTERVAL = 1500;
    private static Battle currentBattle = null;
    final Handler thisBattle = new Handler();
    private final Player player;
    private final Place place;
    private CountDownTimer timer;
    private BattleUnit[] myArmy;
    private BattleUnit[] enemyArmy;
    private Map<String, Integer> myArmyCount;
    private Map<String, Integer> enemyArmyCount;
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

    public static void create(Player player, Place place, GameUnit[] playerArmy) {
        currentBattle = new Battle(player, place, playerArmy);
    }

    public static Battle getBattle() {
        return currentBattle;
    }

    public static void terminateBattle() {
        currentBattle = null;
    }

    public void setActivity(BattleActivity parent, Resources resources) {
        this.parent = parent;
        this.resources = resources;
        this.parent.UpdateUI(myArmyCount, enemyArmyCount, myArmy.length, enemyArmy.length);
    }

    public void start() {
        thisBattle.postDelayed(new Runnable() {
            @Override
            public void run() {
                place.setUnderAttack(true);
                place.updatePlace();
                parent.addMessage(new BattleMessage("The Battle has begun!", null));
                for (int i = 0; i < NUMBER_OF_ROUNDS; i++) {

                    thisBattle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playerTurn();
                        }
                    }, INTERVAL * (2 * i + 1));

                    thisBattle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enemyTurn();

                            player.updatePlayer();
                            place.updatePlace();

                            evaluateBattleStatus();
                        }
                    }, INTERVAL * (2 * i + 2));
                }
            }
        }, 1500);
    }

    private void playerTurn() {
        ActionReport myReport = new ActionReport();

        if (!isDefeated(myArmy) && !isDefeated(enemyArmy)) {
            for (BattleUnit bu : myArmy) {
                if (!bu.isDead()) {
                    bu.getType().act(bu, myArmy, enemyArmy, myReport, myArmyCount, enemyArmyCount, null, place);
                }
            }
            if (myReport.getKills() > 0) {
                enemykilled += myReport.getKills();
                parent.updateEnemyArmy(enemyArmyCount, enemyArmy.length - enemykilled);
            }

            parent.addMessage(getMessageForPlayer(myReport));
        }
    }

    private void enemyTurn() {
        ActionReport enemyReport = new ActionReport();

        if (!isDefeated(myArmy) && !isDefeated(enemyArmy)) {
            for (BattleUnit bu : enemyArmy) {
                if (!bu.isDead()) {
                    bu.getType().act(bu, enemyArmy, myArmy, enemyReport, enemyArmyCount, myArmyCount, player, null);
                }
            }
            if (enemyReport.getKills() > 0) {
                friendlykilled += enemyReport.getKills();
                parent.updateMyArmy(myArmyCount, myArmy.length - friendlykilled);
            }

            parent.addMessage(getMessageForEnemy(enemyReport));
        }
    }

    private void evaluateBattleStatus() {
        boolean myArmyDead = isDefeated(myArmy);
        boolean enemyArmyDead = isDefeated(enemyArmy);
        if (myArmyDead || enemyArmyDead) {
            thisBattle.removeCallbacksAndMessages(null);
            inBattle = false;
            if (enemyArmyDead) {
                victory = true;
            }
            thisBattle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endBattle();
                }
            }, INTERVAL);
        }
    }

    private void endBattle() {

        int exp = enemykilled * ExpReward.UNIT_KILLED.reward;
        if (victory) {
            parent.addMessage(new BattleMessage("The Battle has ended and you are victorious!", player.getPlayerFaction()));
            exp += ExpReward.VICTORY.reward;
            place.setUnderAttack(false);
            place.free();
        } else {
            parent.addMessage(new BattleMessage("The Battle has ended but you have been defeated!", place.getFaction()));
            place.setUnderAttack(false);
            place.updatePlace();
        }

        player.giveExp(exp);
        player.updatePlayer();

        terminateBattle();

        Dialog d = new EndOfBattleDialog(parent, victory, friendlykilled, enemykilled);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(210, 0, 0, 0)));
        d.show();
    }

    private BattleMessage getMessageForPlayer(ActionReport myReport) {
        String message;
        if (myReport.getKills() > 0) {
            message = "Your army has engaged the enemy";
        } else {
            if (myReport.successRate() > 0) {
                message = "Your army hit the enemy but with no kills";
            } else {
                message = "Your army did not hit the enemy";
            }
        }
        return new BattleMessage(message, player.getPlayerFaction());
    }

    private BattleMessage getMessageForEnemy(ActionReport enemyReport) {
        String message;
        if (enemyReport.getKills() > 0) {
            message = "The enemy has engaged you";
        } else {
            if (enemyReport.successRate() > 0) {
                message = "Your army was hit but with no casualties";
            } else {
                message = "Your army did not suffer casualties from the enemy";
            }
        }
        return new BattleMessage(message, place.getFaction());
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

    public void retreat() {
        AlertDialog.Builder builder = new AlertDialog.Builder(parent);
        builder.setTitle("Retreat?");
        builder.setMessage("If you retreat it will count as a defeat. Are you sure?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            thisBattle.removeCallbacksAndMessages(null);
            thisBattle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endBattle();
                }
            }, INTERVAL);
        });
        builder.setNegativeButton("No", (dialog, which) -> {

        });
        AlertDialog d = builder.create();
        d.show();
    }
}
