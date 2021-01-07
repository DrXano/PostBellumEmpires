package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;

public class StructureDialog extends Dialog implements View.OnClickListener{

    Context context;
    private final Player player;
    private final Place place;
    private final int position;

    public StructureDialog(@NonNull Context context, int position, Place place, Player player) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.context = context;
        this.player = player;
        this.place = place;
        this.position = position;
    }

    @Override
    public void onClick(View v) {

    }
}
