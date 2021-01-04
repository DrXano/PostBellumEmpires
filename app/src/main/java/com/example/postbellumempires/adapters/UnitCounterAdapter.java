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

    public UnitCounterAdapter(GameUnit[] mDataset) {
        this.mDataset = mDataset;
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
        holder.unitName.setText(gu.getName());
        final int[] counter = {0};
        holder.counterView.setText(String.valueOf(counter[0]));

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter[0] < gu.getQuantity()) {
                    counter[0]++;
                    holder.counterView.setText(String.valueOf(counter[0]));
                }
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter[0] > 0) {
                    counter[0]--;
                    holder.counterView.setText(String.valueOf(counter[0]));
                }
            }
        });

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
