package com.example.postbellumempires.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.dialogs.ItemDialog;
import com.example.postbellumempires.gameobjects.Item;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ItemViewHolder> {
    private final Item[] mDataset;
    private final Activity parent;

    public InventoryAdapter(Item[] myDataset, Activity parent) {
        mDataset = myDataset;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(inflater.inflate(R.layout.view_holder_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item i = mDataset[position];
        holder.resourceNameView.setText(i.getResourceItem().name);
        holder.resourceQuantityView.setText(String.valueOf(i.getQuantity()));
        holder.resourceImageView.setImageResource(i.getResourceItem().image);
        holder.infoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog d = new ItemDialog(parent,i.getResourceItem());
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                d.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView resourceNameView;
        TextView resourceQuantityView;
        ImageView resourceImageView;
        ImageButton infoImageView;

        public ItemViewHolder(View v) {
            super(v);
            resourceNameView = itemView.findViewById(R.id.resourceName);
            resourceQuantityView = itemView.findViewById(R.id.quantityNumber);
            resourceImageView = itemView.findViewById(R.id.itemImage);
            infoImageView = itemView.findViewById(R.id.item_info_button);
        }
    }
}
