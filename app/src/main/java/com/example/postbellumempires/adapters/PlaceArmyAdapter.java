package com.example.postbellumempires.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.GameUnit;

public class PlaceArmyAdapter extends RecyclerView.Adapter<PlaceArmyAdapter.PlaceUnitViewHolder> {

    private final GameUnit[] mDataset;

    public PlaceArmyAdapter(GameUnit[] mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public PlaceUnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new PlaceUnitViewHolder(inflater.inflate(R.layout.view_holder_levelnunit, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceUnitViewHolder holder, int position) {
        GameUnit gu = mDataset[position];
        holder.unitLevel.setText(String.valueOf(gu.getLevel()));
        holder.unitName.setText(gu.getName());
        holder.unitQuantity.setText(String.valueOf(gu.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class PlaceUnitViewHolder extends RecyclerView.ViewHolder {
        TextView unitLevel;
        TextView unitName;
        TextView unitQuantity;

        public PlaceUnitViewHolder(View v) {
            super(v);
            unitLevel = itemView.findViewById(R.id.placeunitLevel);
            unitName = itemView.findViewById(R.id.placeunitname);
            unitQuantity = itemView.findViewById(R.id.placeunitquantity);
        }
    }
}
