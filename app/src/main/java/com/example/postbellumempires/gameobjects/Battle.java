package com.example.postbellumempires.gameobjects;

import android.app.AlertDialog;
import android.app.Dialog;
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

    private static final int NUMBER_OF_ROUNDS = 30;
    private static final double REMAINING_WARNING = 0.2;
    private static final double LOWER_BOUND = 0.4;
    private static final double HIGHER_BOUND = 0.8;

    private static final int INTERVAL = 1900;
    private static Battle currentBattle = null;
    final Handler thisBattle = new Handler();
    private final Player player;
    private final Place place;
    private CountDownTimer timer;
    private BattleUnit[] myArmy;
    private BattleUnit[] enemyArmy;
    private Map<String, Integer> myArmyCount;
    private Map<String, Integer> enemyArmyCount;
    private boolean myArmyInDangerMsg;
    private boolean enemyArmyInDangerMsg;
    private boolean victory;
    private int friendlykilled;
    private int enemykilled;

    private BattleActivity parent;

    public Battle(Player player, Place place, GameUnit[] playerArmy) {
        this.player = player;
        this.place = place;
        this.myArmyInDangerMsg = true;
        this.enemyArmyInDangerMsg = true;
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

    public void setActivity(BattleActivity parent) {
        this.parent = parent;
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
                    boolean isFirstRound = i == 0;

                    thisBattle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playerTurn(isFirstRound);
                        }
                    }, INTERVAL * (2 * i + 1));

                    thisBattle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enemyTurn(isFirstRound);

                            player.updatePlayer();
                            place.updatePlace();

                            evaluateBattleStatus();
                        }
                    }, INTERVAL * (2 * i + 2));
                }
            }
        }, 1500);
    }

    private void playerTurn(boolean firstRound) {
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

            parent.addMessage(getMessageForPlayer(firstRound, myReport));
        }
    }

    private void enemyTurn(boolean firstRound) {
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

            parent.addMessage(getMessageForEnemy(firstRound, enemyReport));
        }
    }

    private void evaluateBattleStatus() {
        boolean myArmyDead = isDefeated(myArmy);
        boolean enemyArmyDead = isDefeated(enemyArmy);
        if (myArmyDead || enemyArmyDead) {
            thisBattle.removeCallbacksAndMessages(null);
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
            parent.addMessage(new BattleMessage(parent.getResources().getString(R.string.msg_victory), player.getPlayerFaction()));
            exp += ExpReward.VICTORY.reward;
            place.setUnderAttack(false);
            place.free();
        } else {
            parent.addMessage(new BattleMessage(parent.getResources().getString(R.string.msg_defeat), place.getFaction()));
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

    private BattleMessage getMessageForPlayer(boolean firstRound, ActionReport myReport) {
        String message;
        int kills = myReport.getKills();
        double attackRate = myReport.attackRate();
        double healRate = myReport.healRate();
        double boostRate = myReport.boostRate();
        double remaining = (double) (enemyArmy.length - enemykilled) / (double) enemyArmy.length;

        if (firstRound) {
            if (attackRate > 0) {
                if (kills > 0) {
                    message = parent.getResources().getString(R.string.msg_player_1);
                } else {
                    message = parent.getResources().getString(R.string.msg_player_2);
                }
            } else {
                message = parent.getResources().getString(R.string.msg_player_3);
            }
        } else {
            if (remaining <= REMAINING_WARNING && this.enemyArmyInDangerMsg) {
                message = parent.getResources().getString(R.string.msg_player_4);
                this.enemyArmyInDangerMsg = false;
            } else if (attackRate > 0) {
                if (kills > 0) {
                    message = parent.getResources().getString(R.string.msg_player_5);
                } else if (healRate > 0 && boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_player_6);
                } else if (healRate > 0) {
                    message = parent.getResources().getString(R.string.msg_player_7);
                } else if (boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_player_8);
                } else if (attackRate <= LOWER_BOUND) {
                    message = parent.getResources().getString(R.string.msg_player_9);
                } else if (attackRate > LOWER_BOUND && attackRate <= HIGHER_BOUND) {
                    message = parent.getResources().getString(R.string.msg_player_10);
                } else {
                    message = parent.getResources().getString(R.string.msg_player_11);
                }
            } else {
                if (healRate > 0 && boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_player_12);
                } else if (healRate > 0) {
                    message = parent.getResources().getString(R.string.msg_player_13);
                } else if (boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_player_14);
                } else {
                    message = parent.getResources().getString(R.string.msg_player_15);
                }
            }
        }
        return new BattleMessage(message, player.getPlayerFaction());
    }

    private BattleMessage getMessageForEnemy(boolean firstRound, ActionReport enemyReport) {
        String message;
        int kills = enemyReport.getKills();
        double attackRate = enemyReport.attackRate();
        double healRate = enemyReport.healRate();
        double boostRate = enemyReport.boostRate();
        double remaining = (double) (myArmy.length - friendlykilled) / (double) myArmy.length;

        if (firstRound) {
            if (attackRate > 0) {
                if (kills > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_1);
                } else {
                    message = parent.getResources().getString(R.string.msg_enemy_2);
                }
            } else {
                message = parent.getResources().getString(R.string.msg_enemy_3);
            }
        } else {
            if (remaining <= REMAINING_WARNING && this.myArmyInDangerMsg) {
                message = parent.getResources().getString(R.string.msg_enemy_4);
                this.myArmyInDangerMsg = false;
            } else if (attackRate > 0) {
                if (kills > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_5);
                } else if (healRate > 0 && boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_6);
                } else if (healRate > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_7);
                } else if (boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_8);
                } else if (attackRate <= LOWER_BOUND) {
                    message = parent.getResources().getString(R.string.msg_enemy_9);
                } else if (attackRate > LOWER_BOUND && attackRate <= HIGHER_BOUND) {
                    message = parent.getResources().getString(R.string.msg_enemy_10);
                } else {
                    message = parent.getResources().getString(R.string.msg_enemy_11);
                }
            } else {
                if (healRate > 0 && boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_12);
                } else if (healRate > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_13);
                } else if (boostRate > 0) {
                    message = parent.getResources().getString(R.string.msg_enemy_14);
                } else {
                    message = parent.getResources().getString(R.string.msg_enemy_15);
                }
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
        builder.setTitle(parent.getResources().getString(R.string.retreatTitle));
        builder.setMessage(parent.getResources().getString(R.string.retreatMsg));
        builder.setPositiveButton(parent.getResources().getString(R.string.yes), (dialog, which) -> {
            thisBattle.removeCallbacksAndMessages(null);
            thisBattle.postDelayed(new Runnable() {
                @Override
                public void run() {
                    endBattle();
                }
            }, INTERVAL);
        });
        builder.setNegativeButton(parent.getResources().getString(R.string.no), (dialog, which) -> {

        });
        AlertDialog d = builder.create();
        d.show();
    }
}
