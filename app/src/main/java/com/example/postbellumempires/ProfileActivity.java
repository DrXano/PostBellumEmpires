package com.example.postbellumempires;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ProgressBar;

public class ProfileActivity extends AppCompatActivity {

    private ProgressBar userexp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Your Profile");

        this.userexp = findViewById(R.id.profileUserExp);
        userexp.setProgress(75);

    }
}