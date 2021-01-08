package com.example.postbellumempires.enums;

import com.example.postbellumempires.R;

import java.util.HashMap;
import java.util.Map;

public enum Faction {
    OC("Oratio Cult", R.drawable.ocsymbol, R.color.OCprimary, R.color.OCsecondary),
    DR("Dauntless Raiders", R.drawable.drsymbol, R.color.DRprimary, R.color.DRsecondary),
    ES("Erudite Sovereignty", R.drawable.essymbol, R.color.ESprimary, R.color.ESsecondary);

    private static final Map<String, Faction> BY_NAME = new HashMap<>();

    static {
        for (Faction e : values()) {
            BY_NAME.put(e.name, e);
        }
    }

    public final String name;
    public final int symbol;
    public final int primaryColor;
    public final int secondaryColor;

    Faction(String name, int symbol, int primaryColor, int secondaryColor) {
        this.name = name;
        this.symbol = symbol;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public static Faction valueOfName(String name) {
        return BY_NAME.get(name);
    }
}
