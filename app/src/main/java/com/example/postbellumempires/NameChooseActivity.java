package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class NameChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_choose);
    }

    public void choose(View view) {
        Intent intent = new Intent(this, MainGameActivity.class);
        startActivity(intent);
    }
}