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
    Fragment profile = new ProfileFragment();
    Fragment armyMenu = new ArmyMenuFragment();
    Fragment inventory = new InventoryFragment();
    Fragment map = new MapsFragment();
    Fragment current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        current = this.map;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainfrag, this.map)
                .commit();
    }

    public void changeToMap(){
        if(!(current instanceof MapsFragment)){
            current = this.map;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainfrag, current)
                    .addToBackStack(null)
                    .commit();
        }
    };

    public void changeToProfile(){
        if(current instanceof MapsFragment){
            current = this.profile;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainfrag, current)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void changeToArmyMenu(){
        if(current instanceof MapsFragment){
            current = this.armyMenu;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainfrag, current)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void changeToInventory(){
        if(current instanceof MapsFragment){
            current = this.inventory;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainfrag, current)
                    .addToBackStack(null)
                    .commit();
        }
    }
}