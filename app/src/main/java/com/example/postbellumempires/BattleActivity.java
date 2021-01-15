package com.example.postbellumempires;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.adapters.CurrentArmyAdapter;
import com.example.postbellumempires.adapters.MessageAdapter;
import com.example.postbellumempires.enums.UnitType;
import com.example.postbellumempires.gameobjects.Battle;
import com.example.postbellumempires.gameobjects.BattleMessage;
import com.example.postbellumempires.gameobjects.GameUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BattleActivity extends AppCompatActivity {

    Battle battle;
    private RecyclerView playerArmy;
    private RecyclerView placeArmy;
    private RecyclerView events;

    private LinearLayoutManager eventLayout;

    private TextView myRemaining;
    private TextView myTotal;
    private TextView enemyRemaining;
    private TextView enemyTotal;
    private Button retreatButton;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        this.battle = Battle.getBattle();

        this.playerArmy = findViewById(R.id.myArmyRV);
        this.placeArmy = findViewById(R.id.enemyArmyRV);
        this.events = findViewById(R.id.eventsRV);
        this.retreatButton = findViewById(R.id.retreatButton);
        this.myRemaining = findViewById(R.id.myRemaining);
        this.myTotal = findViewById(R.id.myTotal);
        this.enemyRemaining = findViewById(R.id.enemyRemaining);
        this.enemyTotal = findViewById(R.id.enemyTotal);
        int playerColor = getResources().getColor(battle.getPlayer().getPlayerFaction().primaryColor);
        int enemyColor = getResources().getColor(battle.getPlace().getFaction().primaryColor);

        this.playerArmy.setBackground(getResources().getDrawable(battle.getPlayer().getPlayerFaction().background));

        this.placeArmy.setBackground(getResources().getDrawable(battle.getPlace().getFaction().background));

        this.playerArmy.setLayoutManager(new LinearLayoutManager(this));
        this.placeArmy.setLayoutManager(new LinearLayoutManager(this));

        this.eventLayout = new LinearLayoutManager(this);
        this.events.setLayoutManager(this.eventLayout);
        this.adapter = new MessageAdapter(new ArrayList<>(), getResources());
        this.events.setAdapter(adapter);
        battle.setActivity(this, getResources());
    }

    @Override
    protected void onStart() {
        super.onStart();
        battle.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void UpdateUI(Map<String, Integer> myArmyCount, Map<String, Integer> enemyArmyCount, int myArmyTotal, int enemyArmyTotal) {
        this.myTotal.setText(String.valueOf(myArmyTotal));
        this.enemyTotal.setText(String.valueOf(enemyArmyTotal));
        this.updateMyArmy(myArmyCount, myArmyTotal);
        this.updateEnemyArmy(enemyArmyCount, enemyArmyTotal);
    }

    public void addMessage(BattleMessage message) {
        adapter.addMessage(message);
        int index = adapter.getItemCount() - 1;
        adapter.notifyItemInserted(index);
        eventLayout.scrollToPosition(index);
        events.post(new Runnable() {
            @Override
            public void run() {
                View target = eventLayout.findViewByPosition(index);
                if (target != null) {
                    int offset = events.getMeasuredHeight() - target.getMeasuredHeight();
                    eventLayout.scrollToPositionWithOffset(index, offset);
                }
            }
        });
    }

    private int changeOpacity(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        alpha *= 0.5;

        return Color.argb(alpha, red, green, blue);
    }

    public void updateMyArmy(Map<String, Integer> myArmyCount, int myCurrent) {
        GameUnit[] guArr = makeArrayOfUnits(myArmyCount);
        this.myRemaining.setText(String.valueOf(myCurrent));
        this.playerArmy.setAdapter(new CurrentArmyAdapter(guArr));
    }

    public void updateEnemyArmy(Map<String, Integer> enemyArmyCount, int enemyCurrent) {
        GameUnit[] guArr = makeArrayOfUnits(enemyArmyCount);
        this.enemyRemaining.setText(String.valueOf(enemyCurrent));
        this.placeArmy.setAdapter(new CurrentArmyAdapter(guArr));
    }

    private GameUnit[] makeArrayOfUnits(Map<String, Integer> armyCount) {
        List<GameUnit> units = new ArrayList<>();
        for (UnitType t : UnitType.values()) {
            if (armyCount.containsKey(t.name)) {
                units.add(new GameUnit(0, t.name, 0, armyCount.get(t.name), t, null));
            }
        }

        if (units.isEmpty()) {
            return new GameUnit[0];
        } else {
            GameUnit[] unitsArr = new GameUnit[units.size()];
            units.toArray(unitsArr);
            return unitsArr;
        }
    }

    public void retreat(View view) {
        battle.retreat();
    }

    public void finishActivity() {
        finish();
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
        battle.retreat();
    }
}