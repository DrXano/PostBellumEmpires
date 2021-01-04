package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.adapters.UnitCounterAdapter;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ActionDialog extends Dialog implements View.OnClickListener {

    public ImageButton cancel;
    public Button action;
    public Button actionAll;
    public RecyclerView units;
    private Place place;
    private Player player;
    private Context context;
    private TextView actiontitleView;

    public ActionDialog(@NonNull Context context, Place p, Player player) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.context = context;
        this.player = player;
        this.place = p;
    }

    private boolean isFriendly() {
        return this.isFriendly(place, player);
    }

    private boolean isFriendly(Place place, Player player) {
        return (place.getOwnerFaction() == null || place.getOwnerFaction().equals(player.getPFaction()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_dialog);
        action = findViewById(R.id.ADactionButton);
        actionAll = findViewById(R.id.allButton);
        cancel = findViewById(R.id.ADcancelButton);
        actiontitleView = findViewById(R.id.actionTitle);

        if (this.isFriendly()) {
            action.setText("Deploy");
            actionAll.setText("Deploy All");
            actiontitleView.setText("Deploy Units");
        } else {
            action.setText("Attack");
            actionAll.setText("Send All");
            actiontitleView.setText("Attack this Post");
        }

        units = findViewById(R.id.armytodeployRV);
        units.setLayoutManager((new LinearLayoutManager(getContext())));

        List<GameUnit> playerunits = this.player.getArmy().getAvailableUnits();
        GameUnit[] arr = playerunits.toArray(new GameUnit[playerunits.size()]);

        units.setAdapter(new UnitCounterAdapter(arr));

        action.setOnClickListener(this);
        actionAll.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ADactionButton:
                if (this.isFriendly()) {
                    place.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Place p = snapshot.getValue(Place.class);
                                if (isFriendly(p, player)) {
                                    boolean success = true; //p.deployAll(player);
                                    //TODO
                                    if (success) {
                                        if (p.isOccupied()) {
                                            Toast.makeText(context, "Units successfully deployed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            p.occupy(player);
                                            Toast.makeText(context, "You have successfully claimed this post ", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Limit exceeded, cannot deploy", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "This post has been claimed by the enemy", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            } else {
                                Toast.makeText(context, "Error on Deployment", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    place.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Place p = snapshot.getValue(Place.class);
                                if (!isFriendly(p, player)) {
                                    //TODO

                                    place.free();
                                    Toast.makeText(context, "This post has been neutralized", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "This post does not belong to the enemy anymore", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            } else {
                                Toast.makeText(context, "Error on Attack", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                dismiss();
                break;
            case R.id.allButton:
                if (this.isFriendly()) {
                    place.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Place p = snapshot.getValue(Place.class);
                                if (isFriendly(p, player)) {
                                    boolean success = p.deployAll(player);
                                    if (success) {
                                        if (p.isOccupied()) {
                                            Toast.makeText(context, "Units successfully deployed", Toast.LENGTH_SHORT).show();
                                        } else {
                                            p.occupy(player);
                                            Toast.makeText(context, "You have successfully claimed this post ", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Limit exceeded, cannot deploy", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "This post has been claimed by the enemy", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            } else {
                                Toast.makeText(context, "Error on Deployment", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                } else {
                    place.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Place p = snapshot.getValue(Place.class);
                                if (!isFriendly(p, player)) {
                                    List<GameUnit> units = player.getArmy().getAvailableUnits();
                                    player.emptyArmy();
                                    place.free();
                                    Toast.makeText(context, "This post has been neutralized", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "This post does not belong to the enemy anymore", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();
                            } else {
                                Toast.makeText(context, "Error on Attack", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
                break;
            case R.id.ADcancelButton:
                dismiss();
                break;
            default:
                break;
        }
    }
}
