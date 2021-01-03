package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.postbellumempires.R;

public class AttackDialog extends Dialog implements View.OnClickListener {

    public ImageButton cancel;
    public Button attack;

    public AttackDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attack_dialog);
        attack = findViewById(R.id.attackButton2);
        cancel = findViewById(R.id.cancelButton2);
        attack.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attackButton2:
                break;
            case R.id.cancelButton2:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
