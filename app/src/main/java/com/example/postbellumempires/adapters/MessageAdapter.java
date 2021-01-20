package com.example.postbellumempires.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.BattleMessage;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<BattleMessage> mDataset;
    private Resources res;

    public MessageAdapter(List<BattleMessage> mDataset, Resources resources) {
        this.mDataset = mDataset;
        this.res = resources;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MessageViewHolder(inflater.inflate(R.layout.view_holder_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        BattleMessage m = mDataset.get(position);

        if (m.getFaction() == null) {
            holder.layout.setBackground(res.getDrawable(R.drawable.neutral_bg));
        } else {
            holder.layout.setBackground(res.getDrawable(m.getFaction().background));
        }

        holder.messageView.setText(m.getMessage());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addMessage(BattleMessage message) {
        mDataset.add(message);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView messageView;

        public MessageViewHolder(View v) {
            super(v);
            layout = itemView.findViewById(R.id.messageCL);
            messageView = itemView.findViewById(R.id.messageView);
        }
    }
}
