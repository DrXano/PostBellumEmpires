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
import com.example.postbellumempires.enums.PlayerLevel;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.gameobjects.Unit;

import java.util.ArrayList;
import java.util.List;

public class UpgradeMenuAdapter extends RecyclerView.Adapter<UpgradeMenuAdapter.UnitUpgradeViewHolder> {

    private final List<GameUnit> mDataset;
    private Player player;
    private final int unavailableColor;
    private final Context context;

    public UpgradeMenuAdapter(RecyclerView.LayoutManager layoutManager, int unavailableColor, Context context) {
        this.mDataset = new ArrayList<>();
        this.unavailableColor = unavailableColor;
        this.context = context;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    public void addUnit(GameUnit unit){
        this.mDataset.add(unit);
    }

    @NonNull
    @Override
    public UnitUpgradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UnitUpgradeViewHolder(inflater.inflate(R.layout.view_holder_unitupgrade, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnitUpgradeViewHolder holder, int position) {
        GameUnit gu = mDataset.get(position);
        Unit u = gu.getUnit();
        holder.unitName.setText(gu.getName());
        holder.requirementsView.setLayoutManager(new LinearLayoutManager(context));
        if (gu.getLevel() == 0) {
            holder.levelText.setText("");
            holder.unitLevel.setText("");
            holder.upgradeButton.setClickable(false);
            holder.upgradeButton.getBackground().setAlpha(64);
            int availableLevel = PlayerLevel.valueOfUnit(gu.getEType()).level;
            holder.upgradeButton.setText("Reach \n Lvl. " + availableLevel);
        } else if (u.isMaxed()) {
            holder.levelText.setText("Level ");
            holder.unitLevel.setText(String.valueOf(gu.getLevel()));
            holder.upgradeButton.setClickable(false);
            holder.upgradeButton.getBackground().setAlpha(64);
            holder.upgradeButton.setText("Maxed");
        } else {
            Item[] cost = u.getUpgradeCost();
            RecyclerView.Adapter reqAdapter = new RequirementsAdapter(cost, this.player, this.unavailableColor);
            holder.requirementsView.setAdapter(reqAdapter);
            holder.levelText.setText("Level ");
            holder.unitLevel.setText(String.valueOf(gu.getLevel()));
            holder.upgradeButton.setClickable(false);
            holder.upgradeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player.hasEnough(cost)) {
                        player.removeItems(cost);
                        player.levelUpUnit(gu.getEType());
                        player.updatePlayer();
                    } else {
                        Toast.makeText(context, "Insuficient Resources", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class UnitUpgradeViewHolder extends RecyclerView.ViewHolder {
        TextView unitName;
        TextView levelText;
        TextView unitLevel;
        RecyclerView requirementsView;
        Button upgradeButton;

        public UnitUpgradeViewHolder(View v) {
            super(v);
            unitName = itemView.findViewById(R.id.unittoUpText);
            levelText = itemView.findViewById(R.id.levelUpgText);
            unitLevel = itemView.findViewById(R.id.unitlevelupgText);
            requirementsView = itemView.findViewById(R.id.reqsRV);
            upgradeButton = itemView.findViewById(R.id.upgradeButton);
        }
    }
}
