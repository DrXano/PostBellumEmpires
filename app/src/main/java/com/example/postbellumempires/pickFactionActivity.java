package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class pickFactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_faction);
    }

    public void choose(View view) {
        Intent intent = new Intent(this, NameChooseActivity.class);
        startActivity(intent);
    }
}