package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.adapters.StructBuildAdapter;
import com.example.postbellumempires.enums.Structure;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;

import java.util.ArrayList;
import java.util.List;

public class StructureDialog extends Dialog implements View.OnClickListener {

    private final Player player;
    private final Place place;
    private final int position;
    public ImageButton cancel;
    public RecyclerView structures;
    Context context;
    private int unavailableColor;

    public StructureDialog(@NonNull Context context, int position, Place place, Player player, int unavailableColor) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.context = context;
        this.player = player;
        this.place = place;
        this.position = position;
        this.unavailableColor = unavailableColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_structure);
        cancel = findViewById(R.id.DScancelButton);

        structures = findViewById(R.id.structuresRV);
        structures.setLayoutManager(new LinearLayoutManager(getContext()));
        Structure[] strs = Structure.values();
        List<Structure> structslist = new ArrayList<>();
        for (Structure s : strs) {
            if (!s.isEmpty)
                structslist.add(s);
        }
        Structure[] structs = new Structure[structslist.size()];
        structslist.toArray(structs);
        structures.setAdapter(new StructBuildAdapter(StructureDialog.this, structs, this.position, new LinearLayoutManager(context), this.player, this.place, this.unavailableColor, context));
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DScancelButton:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void dismissDialog() {
        dismiss();
    }
}
