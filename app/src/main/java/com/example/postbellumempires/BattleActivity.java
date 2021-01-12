package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.gameobjects.Battle;

public class BattleActivity extends AppCompatActivity {

    Battle battle;
    private RecyclerView playerArmy;
    private RecyclerView placeArmy;
    private RecyclerView events;
    private Button retreatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        this.playerArmy = findViewById(R.id.myArmyRV);
        this.placeArmy = findViewById(R.id.enemyArmyRV);
        this.events = findViewById(R.id.eventsRV);
        this.retreatButton = findViewById(R.id.retreatButton);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}