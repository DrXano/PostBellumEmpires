package com.example.postbellumempires;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.interfaces.InterfaceListener;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainGameActivity extends AppCompatActivity {

    private static final String TAG = "MainGameActivity";
    Fragment map = new MapsFragment();
    private FirebaseAuth mAuth;
    private DatabaseReference playerRef;
    private final DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");
    private Player player;
    private InterfaceListener listener;

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
        if (mAuth.getCurrentUser() != null) {
            this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
            playerRef.addValueEventListener(new ValueEventListener() {
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
            });

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainfrag, this.map)
                    .commit();

        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction()
                .attach(this.map)
                .commit();
        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
        });

        this.LocRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        if(map.isResumed()) {
                            Place p = ds.getValue(Place.class);
                            listener.getMap().addMarker(p.getMarker(getResources()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //listener.loadPlaces();

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