package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.adapters.PlaceArmyAdapter;
import com.example.postbellumempires.adapters.UnitCounterAdapter;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;

import java.util.ArrayList;
import java.util.List;

public class ActionDialog extends Dialog implements View.OnClickListener {

    public ImageButton cancel;
    public Button action;
    public Button actionAll;
    public RecyclerView units;
    private Place place;
    private Player player;

    public ActionDialog(@NonNull Context context, Place p, Player player) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.player = player;
        this.place = p;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attack_dialog);
        action = findViewById(R.id.ADactionButton);
        actionAll = findViewById(R.id.allButton);
        cancel = findViewById(R.id.ADcancelButton);

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
                break;
            case R.id.ADcancelButton:
                dismiss();
                break;
            case  R.id.allButton:
                break;
            default:
                break;
        }
        dismiss();
    }
}
