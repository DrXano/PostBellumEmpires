package com.example.postbellumempires;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.gameobjects.Item;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ItemViewHolder> {
    private Item[] mDataset;

    public InventoryAdapter(Item[] myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.item_view_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item i = mDataset[position];
        holder.resourceNameView.setText(i.getResource());
        holder.resourceQuantityView.setText(String.valueOf(i.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView resourceNameView;
        TextView resourceQuantityView;
        ImageView resourceImageView;

        public ItemViewHolder(View v){
            super(v);
            resourceNameView = itemView.findViewById(R.id.resourceName);
            resourceQuantityView = itemView.findViewById(R.id.quantityNumber);
            resourceImageView = itemView.findViewById(R.id.itemImage);
        }
    }
}
