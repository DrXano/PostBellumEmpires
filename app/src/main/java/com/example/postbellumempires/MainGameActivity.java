package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainGameActivity extends AppCompatActivity {

    private static final String TAG = "MainGameActivity";
    Fragment map = new MapsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainfrag, this.map)
                .commit();
    }

    public void changeToProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void changeToArmyMenu(){
        Intent intent = new Intent(this, ArmyMenuActivity.class);
        startActivity(intent);
    }

    public void changeToInventory(){
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }
}