package com.example.postbellumempires.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.dialogs.StructureDialog;
import com.example.postbellumempires.enums.ExpReward;
import com.example.postbellumempires.enums.Structure;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class StructBuildAdapter extends RecyclerView.Adapter<StructBuildAdapter.StructureViewHolder> {

    private final StructureDialog dialog;
    private final Structure[] mDataset;
    private final int pos;
    private RecyclerView.LayoutManager layoutManager;
    private int unavailableColor;
    private Player player;
    private Place place;
    private Context context;

    public StructBuildAdapter(StructureDialog dialog, Structure[] mDataset, int position, RecyclerView.LayoutManager layoutManager, Player player, Place place, int unavailableColor, Context context) {
        this.dialog = dialog;
        this.mDataset = mDataset;
        this.pos = position;
        this.layoutManager = layoutManager;
        this.unavailableColor = unavailableColor;
        this.player = player;
        this.place = place;
        this.context = context;
    }

    @NonNull
    @Override
    public StructureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new StructureViewHolder(inflater.inflate(R.layout.view_holder_structure, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StructureViewHolder holder, int position) {
        Structure struct = mDataset[position];
        Item[] cost = struct.cost;
        holder.structureNameView.setText(struct.name);
        holder.requirementsView.setLayoutManager(new LinearLayoutManager(context));
        holder.requirementsView.setAdapter(new RequirementsAdapter(cost, this.player, this.unavailableColor));

        holder.buildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.hasEnough(cost)) {
                    place.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Place p = snapshot.getValue(Place.class);
                                boolean sucess = p.buildStructure(struct, pos);
                                if (sucess) {
                                    player.removeItems(cost);
                                    player.giveExp(ExpReward.STRUCTURE_BUILDED.reward);
                                    player.updatePlayer();
                                    Toast.makeText(context, "Structure built with sucess", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(context, "Cannot build structure", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    Toast.makeText(context, "Insuficient Resources", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class StructureViewHolder extends RecyclerView.ViewHolder {
        TextView structureNameView;
        RecyclerView requirementsView;
        Button buildButton;

        public StructureViewHolder(View v) {
            super(v);
            structureNameView = itemView.findViewById(R.id.structName);
            requirementsView = itemView.findViewById(R.id.structreqsRV);
            buildButton = itemView.findViewById(R.id.buildButton);
        }
    }
}
