package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.postbellumempires.BattleActivity;
import com.example.postbellumempires.R;
import com.example.postbellumempires.enums.ExpReward;

public class EndOfBattleDialog extends Dialog implements View.OnClickListener {

    public CardView holder;

    public TextView resulttitle;

    public TextView killedText;
    public TextView killCount;
    public TextView lostText;
    public TextView lostCount;

    public TextView expforkills;
    public TextView expkillsearned;
    public TextView victorybonustext;
    public TextView victorybonus;

    public TextView totalearned;
    public Button dismissBtn;

    BattleActivity activity;
    boolean victory;
    int friendlykilled;
    int enemykilled;

    public EndOfBattleDialog(@NonNull Context context, boolean victory, int friendlykilled, int enemykilled) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        activity = (BattleActivity) context;
        this.victory = victory;
        this.friendlykilled = friendlykilled;
        this.enemykilled = enemykilled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_end_of_battle);
        updateUI();
    }

    private void updateUI() {
        int killexp = enemykilled * ExpReward.UNIT_KILLED.reward;
        int victoryexp = victory ? ExpReward.VICTORY.reward : 0;
        int total = killexp + victoryexp;

        holder = findViewById(R.id.dialogHolder);

        String result = activity.getResources().getString(victory ? R.string.victory : R.string.defeat);
        resulttitle = findViewById(R.id.resulttitle);
        resulttitle.setText(result);

        killedText = findViewById(R.id.killedText);
        killedText.setText(activity.getResources().getString(R.string.unitskilled));
        killCount = findViewById(R.id.killCount);
        killCount.setText(String.valueOf(enemykilled));

        lostText = findViewById(R.id.lostText);
        lostText.setText(activity.getResources().getString(R.string.unitslost));
        lostCount = findViewById(R.id.lostCount);
        lostCount.setText(String.valueOf(friendlykilled));

        expforkills = findViewById(R.id.expforkills);
        expforkills.setText(activity.getResources().getString(R.string.expkills));
        expkillsearned = findViewById(R.id.expkillsearned);
        String killHolder = "+" + killexp + " exp";
        expkillsearned.setText(killHolder);

        if (victory) {
            victorybonustext = findViewById(R.id.victorybonustext);
            victorybonustext.setText(activity.getResources().getString(R.string.victorybonus));
            victorybonus = findViewById(R.id.victorybonus);
            String victoryHolder = "+" + victoryexp + " exp";
            victorybonus.setText(victoryHolder);
        }

        totalearned = findViewById(R.id.totalearned);
        String totalHolder = "Total: +" + total + " exp";
        totalearned.setText(totalHolder);
        dismissBtn = findViewById(R.id.dismissbutton);
        dismissBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dismissbutton:
                dismiss();
                activity.finishActivity();
                break;
        }
    }
}
