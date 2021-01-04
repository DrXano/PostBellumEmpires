package com.example.postbellumempires.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.gameobjects.Unit;

public class TrainMenuAdapter extends RecyclerView.Adapter<TrainMenuAdapter.UnitTrainViewHolder> {

    private final GameUnit[] mDataset;
    private Player player;
    private RecyclerView.LayoutManager layoutManager;
    private int unavailableColor;
    private Context context;

    public TrainMenuAdapter(GameUnit[] mDataset, Player player, RecyclerView.LayoutManager layoutManager, int unavailableColor, Context context) {
        this.mDataset = mDataset;
        this.player = player;
        this.layoutManager = layoutManager;
        this.unavailableColor = unavailableColor;
        this.context = context;
    }

    @NonNull
    @Override
    public UnitTrainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UnitTrainViewHolder(inflater.inflate(R.layout.unittrain_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnitTrainViewHolder holder, int position) {
        GameUnit gu = mDataset[position];
        Unit u = gu.getUnit();
        holder.unitNameView.setText(gu.getName());
        holder.unitSizeView.setText(String.valueOf(gu.getSize()));
        holder.requirementsView.setLayoutManager(this.layoutManager);

        if (gu.getLevel() == 0) {
            holder.levelTextView.setText("");
            holder.unitLevelView.setText("");
            holder.trainButton.setClickable(false);
            holder.trainButton.getBackground().setAlpha(64);
            holder.trainButton.setText("Locked");
        } else {
            Item[] cost = u.getCost();
            RecyclerView.Adapter reqAdapter = new RequirementsAdapter(cost, this.player, this.unavailableColor);
            holder.requirementsView.setAdapter(reqAdapter);
            holder.levelTextView.setText("Level ");
            holder.unitLevelView.setText(String.valueOf(gu.getLevel()));
            holder.trainButton.setClickable(true);
            holder.trainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.hasEnough(cost)) {
                        player.remove(cost);
                        player.addUnit(gu.getEType(), 1);
                        player.updatePlayer();
                    } else {
                        Toast.makeText(context, "Insuficient Resources", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class UnitTrainViewHolder extends RecyclerView.ViewHolder {
        TextView unitNameView;
        TextView unitSizeView;
        TextView unitLevelView;
        TextView levelTextView;
        RecyclerView requirementsView;
        Button trainButton;

        public UnitTrainViewHolder(View v) {
            super(v);
            unitNameView = itemView.findViewById(R.id.unitName);
            unitSizeView = itemView.findViewById(R.id.unitsize);
            unitLevelView = itemView.findViewById(R.id.unitLevel);
            levelTextView = itemView.findViewById(R.id.leveltext);
            requirementsView = itemView.findViewById(R.id.trainreqRV);
            trainButton = itemView.findViewById(R.id.trainbuttonView);
        }
    }
}
