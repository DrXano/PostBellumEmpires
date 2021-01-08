package com.example.postbellumempires.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.GameUnit;

public class CurrentArmyAdapter extends RecyclerView.Adapter<CurrentArmyAdapter.EntityViewHolder> {

    private final GameUnit[] mDataset;

    public CurrentArmyAdapter(GameUnit[] mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public EntityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EntityViewHolder(inflater.inflate(R.layout.view_holder_entity, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EntityViewHolder holder, int position) {
        GameUnit gu = mDataset[position];
        holder.entityView.setText(gu.getName());
        holder.quantityView.setText(String.valueOf(gu.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class EntityViewHolder extends RecyclerView.ViewHolder {
        TextView entityView;
        TextView quantityView;

        public EntityViewHolder(View v) {
            super(v);
            entityView = itemView.findViewById(R.id.entityTV);
            quantityView = itemView.findViewById(R.id.quantityTV);
        }
    }
}
