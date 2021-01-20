package com.example.postbellumempires.interfaces;

import com.example.postbellumempires.gameobjects.Player;
import com.google.android.gms.maps.GoogleMap;

public interface InterfaceListener {
    void updateUI(Player p);

    GoogleMap getMap();
}
