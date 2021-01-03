package com.example.postbellumempires;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.postbellumempires.gameobjects.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference playerRef;

    private TextView playerName;
    private TextView playerFaction;
    private TextView playerLevel;
    private ProgressBar playerProgress;
    private TextView playerExp;
    private TextView playerMaxExp;

    private ImageView symbol;

    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar myToolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Your Profile");

        this.mAuth = FirebaseAuth.getInstance();
        this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());

        this.playerName = findViewById(R.id.pfName);
        this.playerFaction = findViewById(R.id.pfFaction);
        this.playerLevel = findViewById(R.id.pfLevel);
        this.playerProgress = findViewById(R.id.pfUserExp);
        this.playerExp = findViewById(R.id.pfExp);
        this.playerMaxExp = findViewById(R.id.pfMaxExp);
        this.symbol = findViewById(R.id.factionSymbol);

        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Player p = snapshot.getValue(Player.class);
                    updateUI(p);
                } else {
                    Toast.makeText(ProfileActivity.this, "Player information was not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(Player p) {
        this.playerName.setText(p.getInGameName());
        this.playerFaction.setText(p.getPFaction());
        this.playerLevel.setText(String.valueOf(p.getLevel()));

        this.playerProgress.setMax(p.getMaxExp());
        this.playerProgress.setProgress(p.getExp());

        this.playerExp.setText(String.valueOf(p.getExp()));
        this.playerMaxExp.setText(String.valueOf(p.getMaxExp()));

        switch (p.getPlayerFaction()) {
            case OC:
                color = getResources().getColor(R.color.OCprimary);
                this.playerProgress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                this.symbol.setImageResource(R.drawable.ocsymbol);
                this.symbol.setColorFilter(color);
                break;
            case DR:
                color = getResources().getColor(R.color.DRprimary);
                this.playerProgress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                this.symbol.setImageResource(R.drawable.drsymbol);
                this.symbol.setColorFilter(color);
                break;
            case ES:
                color = getResources().getColor(R.color.ESprimary);
                this.playerProgress.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                this.symbol.setImageResource(R.drawable.essymbol);
                this.symbol.setColorFilter(color);
                break;
        }
    }
}