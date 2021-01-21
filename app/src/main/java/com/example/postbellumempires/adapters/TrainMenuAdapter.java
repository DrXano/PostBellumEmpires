package com.example.postbellumempires.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.dialogs.UnitDialog;
import com.example.postbellumempires.enums.PlayerLevel;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.gameobjects.Unit;

import java.util.ArrayList;
import java.util.List;

public class TrainMenuAdapter extends RecyclerView.Adapter<TrainMenuAdapter.UnitTrainViewHolder> {

    private final List<GameUnit> mDataset;
    private final int unavailableColor;
    private final Context context;
    private Player player;

    public TrainMenuAdapter(int unavailableColor, Context context) {
        mDataset = new ArrayList<>();
        this.unavailableColor = unavailableColor;
        this.context = context;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addUnit(GameUnit unit) {
        this.mDataset.add(unit);
    }

    @NonNull
    @Override
    public UnitTrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UnitTrainViewHolder(inflater.inflate(R.layout.view_holder_unittrain, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnitTrainViewHolder holder, int position) {
        GameUnit gu = mDataset.get(position);
        Unit u = gu.getUnit();
        holder.unitNameView.setText(gu.getName());
        holder.unitSizeView.setText(String.valueOf(gu.getSize()));
        holder.requirementsView.setLayoutManager(new LinearLayoutManager(context));

        Item[] cost = gu.getEType().trainCost;
        RequirementsAdapter reqAdapter = new RequirementsAdapter(cost, this.player, this.unavailableColor);
        holder.requirementsView.setAdapter(reqAdapter);
        if (gu.getLevel() == 0) {
            holder.levelTextView.setText("");
            holder.unitLevelView.setText("");
            holder.trainButton.setClickable(false);
            holder.infoView.setClickable(false);
            holder.trainButton.getBackground().setAlpha(64);
            int availableLevel = PlayerLevel.valueOfUnit(gu.getEType()).level;
            holder.trainButton.setText("Reach \n Lvl. " + availableLevel);
        } else {
            holder.levelTextView.setText("Level ");
            holder.unitLevelView.setText(String.valueOf(gu.getLevel()));
            holder.trainButton.setClickable(true);
            holder.infoView.setClickable(true);
            holder.trainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.hasEnough(cost)) {
                        boolean result = player.addUnit(gu.getEType(), 1);
                        if (result) {
                            player.removeItems(cost);
                            player.updatePlayer();
                        } else {
                            Toast.makeText(context, "Can't train more, capacity exceeded", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Insuficient Resources", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.infoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog d = new UnitDialog(context, gu);
                    d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                    d.show();
                }
            });
        }

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public static class UnitTrainViewHolder extends RecyclerView.ViewHolder {
        TextView unitNameView;
        TextView unitSizeView;
        TextView unitLevelView;
        TextView levelTextView;
        RecyclerView requirementsView;
        Button trainButton;
        ImageButton infoView;

        public UnitTrainViewHolder(View v) {
            super(v);
            unitNameView = itemView.findViewById(R.id.unitName);
            unitSizeView = itemView.findViewById(R.id.unitsize);
            unitLevelView = itemView.findViewById(R.id.unitLevel);
            levelTextView = itemView.findViewById(R.id.leveltext);
            requirementsView = itemView.findViewById(R.id.trainreqRV);
            trainButton = itemView.findViewById(R.id.trainbuttonView);
            infoView = itemView.findViewById(R.id.unit_info_button);
        }


    }
}
