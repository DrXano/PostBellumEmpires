package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.MainGameActivity;
import com.example.postbellumempires.R;
import com.example.postbellumempires.adapters.PlaceArmyAdapter;
import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.PlaceArmy;
import com.example.postbellumempires.gameobjects.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class PlaceDialog extends Dialog implements View.OnClickListener {

    private final MainGameActivity parentActivity;
    private final DatabaseReference PlaceRef;
    public TextView PlaceName;
    public TextView OwnerFaction;
    public TextView Owner;
    public TextView Reward;
    public TextView currcap;
    public TextView maxcap;
    public ImageView symbol;
    public ImageButton cancel;
    public Button action;
    public Button collect;
    public RecyclerView armyView;
    Context context;
    private boolean friendly;
    private int color;
    private Place p;

    public PlaceDialog(@NonNull Context context, String id) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.context = context;
        this.parentActivity = (MainGameActivity) context;
        this.PlaceRef = FirebaseDatabase.getInstance().getReference("places").child(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_dialog);
        PlaceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Place p = snapshot.getValue(Place.class);
                    updateUI(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(Place p) {
        Player player = parentActivity.getPlayer();
        this.p = p;
        action = findViewById(R.id.actionButton);
        collect = findViewById(R.id.collectionButton);
        cancel = findViewById(R.id.cancelButton);
        PlaceName = findViewById(R.id.placeTitle);
        OwnerFaction = findViewById(R.id.factionInPlace);
        Owner = findViewById(R.id.ownerInPlace);
        Reward = findViewById(R.id.ResourceReward);
        currcap = findViewById(R.id.currCap);
        maxcap = findViewById(R.id.maxcap);
        symbol = findViewById(R.id.ownerFactionSymbol);
        armyView = findViewById(R.id.placeArmyRV);
        armyView.setLayoutManager(new LinearLayoutManager(getContext()));

        PlaceArmy army = p.getArmy();

        if(army != null && army.getUnits() != null) {
            this.armyView.setAdapter(new PlaceArmyAdapter(army.getUnitsArray()));
        }

        PlaceName.setText(p.getName());

        if (p.getFaction() != null) {
            switch (p.getFaction()) {
                case OC:
                    color = getContext().getResources().getColor(R.color.OCprimary);
                    this.symbol.setImageResource(R.drawable.ocsymbol);
                    this.symbol.setColorFilter(color);
                    break;
                case DR:
                    color = getContext().getResources().getColor(R.color.DRprimary);
                    this.symbol.setImageResource(R.drawable.drsymbol);
                    this.symbol.setColorFilter(color);
                    break;
                case ES:
                    color = getContext().getResources().getColor(R.color.ESprimary);
                    this.symbol.setImageResource(R.drawable.essymbol);
                    this.symbol.setColorFilter(color);
                    break;
                default:
                    color = getContext().getResources().getColor(R.color.black);
                    this.symbol.setImageDrawable(null);
                    break;
            }
        } else {
            color = getContext().getResources().getColor(R.color.black);
            this.symbol.setImageDrawable(null);
        }

        String owner = p.getOwner();
        String ownerFaction = p.getOwnerFaction();
        String reward = p.getResourceRewardType().name();
        if (reward != null) {
            Reward.setText(reward);
        }

        currcap.setText(String.valueOf(p.getCapacity()));
        maxcap.setText(String.valueOf(p.getMaxCapacity()));

        Owner.setText((owner == null) ? "---" : owner);
        OwnerFaction.setText((ownerFaction == null) ? "---" : ownerFaction);
        this.friendly = (p.getOwnerFaction() == null || p.getOwnerFaction().equals(player.getPFaction()));

        if (this.friendly) {
            action.setText("Deploy Troops");
            collect.setText("Collect");
        } else {
            action.setText("Attack");
            collect.setText("Steal");
        }

        action.setOnClickListener(this);
        collect.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionButton:
                if (friendly) {
                    this.p.occupy(parentActivity.getPlayer());
                } else {
                    Dialog d = new ActionDialog(context,this.p,parentActivity.getPlayer());
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                    d.show();
                }
                break;
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.collectionButton:
                Player player = parentActivity.getPlayer();
                GameResource res = this.p.getResourceRewardType();

                Random r = new Random();
                int min = 400;
                int max = 500;
                int reward = (int) ((r.nextInt(max - min) + min) * bonusMultiplier(player));
                player.addItem(res, reward);
                player.updatePlayer();
                Toast.makeText(context, reward + " " + Reward.getText().toString() + " acquired", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private double bonusMultiplier(Player player) {
        if (!this.friendly) {
            return 0.5;
        } else {
            if (p.getOwner() != null && p.getOwner().equals(player.getInGameName())) {
                return 1.5;
            } else {
                return 1.0;
            }
        }
    }
}
