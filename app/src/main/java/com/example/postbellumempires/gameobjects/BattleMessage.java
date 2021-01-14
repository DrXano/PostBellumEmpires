package com.example.postbellumempires.gameobjects;

import android.graphics.Color;

public class BattleMessage {

    private final String message;
    private final int color;

    public BattleMessage(String message, int color) {
        this.message = message;
        this.color = changeOpacity(color);
    }

    public String getMessage() {
        return message;
    }

    public int getColor() {
        return color;
    }

    private int changeOpacity(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        alpha *= 0.5;

        return Color.argb(alpha, red, green, blue);
    }
}
