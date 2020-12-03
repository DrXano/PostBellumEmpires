package com.example.postbellumempires;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

public class PostDialog extends Dialog implements View.OnClickListener {
    public ImageButton cancel;
    public Button attack;
    Context context;

    public PostDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_dialog);
        attack = findViewById(R.id.attackButton);
        cancel = findViewById(R.id.cancelButton);
        attack.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attackButton:
                Dialog d = new AttackDialog(context);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                d.show();
                break;
            case R.id.cancelButton:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
