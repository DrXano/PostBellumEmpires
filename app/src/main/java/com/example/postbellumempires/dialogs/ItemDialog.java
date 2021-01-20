package com.example.postbellumempires.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.postbellumempires.R;
import com.example.postbellumempires.enums.GameResource;

public class ItemDialog extends Dialog implements View.OnClickListener {

    public ImageButton cancel;
    public ImageView image;
    public TextView itemName;
    public TextView description;

    private Activity activity;
    private GameResource item;

    public ItemDialog(@NonNull Context context, GameResource item) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        activity = (Activity) context;
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_item);

        image = findViewById(R.id.item_image);
        itemName = findViewById(R.id.item_name);
        description = findViewById(R.id.item_desc);
        cancel = findViewById(R.id.item_cancel_btn);

        image.setImageResource(item.image);
        itemName.setText(item.name);
        description.setText(activity.getResources().getString(item.description));

        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_cancel_btn:
                dismiss();
                break;
            default:
                break;
        }
    }
}
