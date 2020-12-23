package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.postbellumempires.LoginActivity;
import com.example.postbellumempires.MainGameActivity;
import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaceDialog extends Dialog implements View.OnClickListener {

    private MainGameActivity parentActivity;
    private DatabaseReference PlaceRef;
    private boolean friendly;
    private Place p;

    public TextView PlaceName;
    public TextView OwnerFaction;
    public TextView Owner;
    public TextView Reward;
    public TextView currcap;
    public TextView maxcap;

    public ImageButton cancel;
    public Button action;
    public Button collect;
    Context context;

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
                Place p = snapshot.getValue(Place.class);
                updateUI(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(Place p) {
        Player player = parentActivity.getPlayer();
        this.friendly = p.getOwnerFaction() == null || p.getOwnerFaction().equals(player.getPlayerFaction());
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
        PlaceName.setText(p.getName());

        if(friendly){
            action.setText("Deploy Troops");
            collect.setText("Collect");
        }else{
            action.setText("Attack");
            collect.setText("Steal");
        }

        String owner = p.getOwner();
        String ownerFaction = p.getOwnerFaction();
        String reward = p.getResourceRewardType().name();
        if(reward != null){
            Reward.setText(reward);
        }

        currcap.setText(String.valueOf(p.getCapacity()));
        maxcap.setText(String.valueOf(p.getMaxCapacity()));

        Owner.setText((owner == null)?"---":owner);
        OwnerFaction.setText((ownerFaction == null)?"---":ownerFaction);
        action.setOnClickListener(this);
        collect.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionButton:
                if(friendly){
                    this.p.occupy(parentActivity.getPlayer());
                }else {
                    Dialog d = new AttackDialog(context);
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                    d.show();
                }
                break;
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.collectionButton:
                Toast.makeText(context, Reward.getText().toString() + " acquired", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}