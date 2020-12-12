package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.interfaces.MapListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainGameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference playerRef;
    private MapListener listener;

    private static final String TAG = "MainGameActivity";
    Fragment map = new MapsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        this.listener = (MapListener) this.map;
        this.mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null) {
            this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
            playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Player p = snapshot.getValue(Player.class);
                        listener.updateUI(p);
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
        }else{
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
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

    public void logout(){
        mAuth.signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}