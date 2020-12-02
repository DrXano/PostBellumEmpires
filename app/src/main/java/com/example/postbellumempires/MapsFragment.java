package com.example.postbellumempires;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = "MapsFragment";

    FloatingActionButton menu;
    FloatingActionButton inventory;
    FloatingActionButton armymenu;
    FloatingActionButton profile;
    FloatingActionButton logout;

    private boolean clicked = false;
    private MainGameActivity parentActivity;

    private Animation fromBottom;
    private Animation toBottom;

    private ProgressBar userexp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        parentActivity = (MainGameActivity) getActivity();

        fromBottom = AnimationUtils.loadAnimation(this.getActivity(),R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this.getActivity(),R.anim.to_bottom_anim);

        menu = view.findViewById(R.id.menuButton);
        inventory = view.findViewById(R.id.inventoryButton);
        armymenu = view.findViewById(R.id.armyMenuButton);
        profile = view.findViewById(R.id.profileButton);
        logout = view.findViewById(R.id.logoutButton);

        this.userexp = view.findViewById(R.id.userExp);
        userexp.setProgress(75);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.changeToInventory();
            }
        });

        armymenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.changeToArmyMenu();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.changeToProfile();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention");
        builder.setMessage("You are about to log out. Are you sure you want to leave?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finalLogOut();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog d = builder.create();
        d.show();
    }

    private void finalLogOut() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setAnimation(Boolean clicked) {
        if(!clicked){
            profile.startAnimation(fromBottom);
            armymenu.startAnimation(fromBottom);
            inventory.startAnimation(fromBottom);
            logout.startAnimation(fromBottom);
        }else{
            profile.startAnimation(toBottom);
            armymenu.startAnimation(toBottom);
            inventory.startAnimation(toBottom);
            logout.startAnimation(toBottom);
        }
    }

    private void setVisibility(Boolean clicked) {
        if(!clicked){
            profile.setVisibility(View.VISIBLE);
            profile.setClickable(true);
            armymenu.setVisibility(View.VISIBLE);
            armymenu.setClickable(true);
            inventory.setVisibility(View.VISIBLE);
            inventory.setClickable(true);
            logout.setVisibility(View.VISIBLE);
            logout.setClickable(true);
        }else{
            profile.setVisibility(View.INVISIBLE);
            profile.setClickable(false);
            armymenu.setVisibility(View.INVISIBLE);
            armymenu.setClickable(false);
            inventory.setVisibility(View.INVISIBLE);
            inventory.setClickable(false);
            logout.setVisibility(View.INVISIBLE);
            logout.setClickable(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng myPos = new LatLng(38.715530421468195, -9.217587115772087);
        LatLng dummy = new LatLng(38.71652427178074, -9.215800978110437);

        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(),R.raw.mapstyle));
        googleMap.setMinZoomPreference(18.0f);
        googleMap.setMaxZoomPreference(21.0f);
        googleMap.setBuildingsEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        googleMap.addMarker(new MarkerOptions().position(dummy));
        googleMap.addMarker(new MarkerOptions().position(myPos));
        CameraPosition cp = new CameraPosition.Builder().target(myPos).tilt(70).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(Marker marker) {
                Dialog d = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
                d.setContentView(R.layout.place_menu);
                d.setCancelable(true);
                d.show();
                return true;
            }
        });
    }
}