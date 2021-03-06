package com.example.postbellumempires;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.interfaces.InterfaceListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainGameActivity extends AppCompatActivity {

    private static final String TAG = "MainGameActivity";

    private final DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");
    Fragment map = new MapsFragment();
    private FirebaseAuth mAuth;
    private DatabaseReference playerRef;
    private Player player;
    private InterfaceListener listener;

    private ValueEventListener playerListener;

    private ChildEventListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }

        this.listener = (InterfaceListener) this.map;
        this.mAuth = FirebaseAuth.getInstance();
        this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainfrag, this.map)
                .commit();


        playerListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (map.isResumed()) {
                        Player p = snapshot.getValue(Player.class);
                        player = p;
                        listener.updateUI(p);
                    }
                } else {
                    Toast.makeText(MainGameActivity.this, "Player information was not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        locationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && map.isResumed()) {
                    Place p = snapshot.getValue(Place.class);
                    listener.getMap().addMarker(p.getMarker(getResources()));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && map.isResumed()) {
                    Place p = snapshot.getValue(Place.class);
                    listener.getMap().addMarker(p.getMarker(getResources()));
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        playerRef.addListenerForSingleValueEvent(playerListener);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void changeToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void changeToArmyMenu() {
        Intent intent = new Intent(this, ArmyMenuActivity.class);
        startActivity(intent);
    }

    public void changeToInventory() {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    public void changeToAbout() {
        Intent intent = new Intent(this, DevTeamActivity.class);
        startActivity(intent);
    }

    public void changeToHelp() {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void logout() {
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().beginTransaction()
                .detach(this.map)
                .commit();

        this.playerRef.removeEventListener(playerListener);

        this.LocRef.removeEventListener(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction()
                .attach(this.map)
                .commit();

        this.playerRef.addValueEventListener(playerListener);

        this.LocRef.addChildEventListener(locationListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    @SuppressLint("MissingSuperCall")
    public void onBackPressed() {
    }
}