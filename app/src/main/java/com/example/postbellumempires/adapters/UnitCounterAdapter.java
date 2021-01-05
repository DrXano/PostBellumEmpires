package com.example.postbellumempires.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.GameUnit;

public class UnitCounterAdapter extends RecyclerView.Adapter<UnitCounterAdapter.UnitCounterViewHolder> {

    private final GameUnit[] mDataset;
    private final GameUnit[] resultSet;

    public UnitCounterAdapter(GameUnit[] mDataset) {
        this.mDataset = mDataset;
        this.resultSet = new GameUnit[mDataset.length];
    }

    @NonNull
    @Override
    public UnitCounterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new UnitCounterViewHolder(inflater.inflate(R.layout.unit_quantity_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UnitCounterViewHolder holder, int position) {
        GameUnit gu = mDataset[position];
        GameUnit guu = gu.clone();
        guu.setQuantity(0);
        resultSet[position] = guu;
        holder.unitName.setText(gu.getName());
        holder.counterView.setText(String.valueOf(0));

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultSet[position].getQuantity() < gu.getQuantity()) {
                    resultSet[position].setQuantity(resultSet[position].getQuantity() + 1);
                    holder.counterView.setText(String.valueOf(resultSet[position].getQuantity()));
                }
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultSet[position].getQuantity() > 0) {
                    resultSet[position].setQuantity(resultSet[position].getQuantity() - 1);
                    holder.counterView.setText(String.valueOf(resultSet[position].getQuantity()));
                }
            }
        });

    }

    public GameUnit[] getUnitsToDeploy() {
        return this.resultSet;
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }


    public static class UnitCounterViewHolder extends RecyclerView.ViewHolder {
        TextView unitName;
        TextView counterView;
        ImageButton increment;
        ImageButton decrement;

        public UnitCounterViewHolder(View v) {
            super(v);
            unitName = itemView.findViewById(R.id.unitNameToSend);
            counterView = itemView.findViewById(R.id.counter);
            increment = itemView.findViewById(R.id.incrementButton);
            decrement = itemView.findViewById(R.id.decrementButton);
        }
    }
}
