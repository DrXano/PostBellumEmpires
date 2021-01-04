package com.example.postbellumempires.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Player;

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsAdapter.RequirementsViewHolder> {
    private final Item[] mDataset;
    private Player player;
    private int unavailableColor;

    public RequirementsAdapter(Item[] mDataset, Player player, int unavailableColor) {
        this.mDataset = mDataset;
        this.player = player;
        this.unavailableColor = unavailableColor;
    }

    @NonNull
    @Override
    public RequirementsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RequirementsViewHolder(inflater.inflate(R.layout.entity_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequirementsViewHolder holder, int position) {
        Item i = mDataset[position];
        holder.entityView.setText(i.getResource());
        holder.quantityView.setText(String.valueOf(i.getQuantity()));
        if (!player.hasEnough(i)) {
            holder.quantityView.setTextColor(this.unavailableColor);
            holder.cross.setTextColor(this.unavailableColor);
            holder.entityView.setTextColor(this.unavailableColor);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class RequirementsViewHolder extends RecyclerView.ViewHolder {
        TextView entityView;
        TextView cross;
        TextView quantityView;

        public RequirementsViewHolder(View v) {
            super(v);
            entityView = itemView.findViewById(R.id.entityTV);
            cross = itemView.findViewById(R.id.entitycross);
            quantityView = itemView.findViewById(R.id.quantityTV);
        }
    }
}
