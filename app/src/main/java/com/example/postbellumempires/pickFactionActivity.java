package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.postbellumempires.enums.Faction;

public class pickFactionActivity extends AppCompatActivity {

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_faction);
        this.email = getIntent().getStringExtra("email");
        this.password = getIntent().getStringExtra("pass");
    }

    public void chooseA(View view) {
        nextStep(Faction.A);
    }

    public void chooseB(View view) {
        nextStep(Faction.B);
    }

    public void chooseC(View view) {
        nextStep(Faction.C);
    }

    public void cancel(View view){
        startActivity(new Intent(this, RegisterActivity.class));
        finish();
    }

    void nextStep(Faction faction){
        Intent intent = new Intent(this, NameChooseActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("pass", password);
        intent.putExtra("faction", faction.name());
        startActivity(intent);
        finish();
    }
}