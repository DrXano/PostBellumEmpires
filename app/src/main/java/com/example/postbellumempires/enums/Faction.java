package com.example.postbellumempires.enums;

import com.example.postbellumempires.R;

import java.util.HashMap;
import java.util.Map;

public enum Faction {
    OC("Oratio Cult", R.drawable.ocsymbol, R.color.OCprimary, R.color.OCsecondary, R.drawable.oc_transparent_bg),
    DR("Dauntless Raiders", R.drawable.drsymbol, R.color.DRprimary, R.color.DRsecondary, R.drawable.dr_transparent_bg),
    ES("Erudite Sovereignty", R.drawable.essymbol, R.color.ESprimary, R.color.ESsecondary, R.drawable.es_transparent_bg);

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
    public final int background;

    Faction(String name, int symbol, int primaryColor, int secondaryColor, int background) {
        this.name = name;
        this.symbol = symbol;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.background = background;
    }

    public static Faction valueOfName(String name) {
        return BY_NAME.get(name);
    }
}
