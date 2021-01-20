package com.example.postbellumempires.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.postbellumempires.R;
import com.example.postbellumempires.gameobjects.GameUnit;

public class UnitDialog extends Dialog implements View.OnClickListener{

    private final Activity activity;
    private final GameUnit unit;

    public UnitDialog(@NonNull Context context, GameUnit unit) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        activity = (Activity) context;
        this.unit = unit;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unit_info);

        TextView name = findViewById(R.id.unit_info_name);
        TextView description = findViewById(R.id.unit_info_desc);
        ImageButton cancel = findViewById(R.id.unit_cancel_btn);
        TextView health = findViewById(R.id.unit_health);
        TextView attack = findViewById(R.id.unit_attack);
        TextView armor = findViewById(R.id.unit_armor);
        TextView speed = findViewById(R.id.unit_speed);

        name.setText(unit.getEType().name);
        description.setText(activity.getResources().getString(unit.getEType().description));

        String healthStr = "Health: " + unit.getStats().getHealth();
        String attackStr = "Attack: " + unit.getStats().getAttack();
        String  armorStr = "Armor: " + unit.getStats().getArmor();
        String  speedStr = "Speed: " + unit.getStats().getSpeed();

        health.setText(healthStr);
        attack.setText(attackStr);
        armor.setText(armorStr);
        speed.setText(speedStr);

        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.unit_cancel_btn:
                dismiss();
                break;
            default:
                break;
        }
    }
}
