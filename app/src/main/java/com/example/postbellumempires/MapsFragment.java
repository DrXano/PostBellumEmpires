package com.example.postbellumempires;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.postbellumempires.dialogs.PlaceDialog;
import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.interfaces.InterfaceListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MapsFragment extends Fragment implements OnMapReadyCallback, InterfaceListener {

    private static final int TILT = 60;
    private static final float MAX_ZOOM = 21.0f;
    private static final float MIN_ZOOM = 18.0f;

    private static final String TAG = "MapsFragment";
    private final DatabaseReference LocRef = FirebaseDatabase.getInstance().getReference("places");
    FloatingActionButton menu;

    FloatingActionButton inventory;
    FloatingActionButton armymenu;
    FloatingActionButton profile;
    FloatingActionButton logout;
    FloatingActionButton help;
    FloatingActionButton team;
    private boolean clicked = false;
    private MainGameActivity parentActivity;

    private Animation fromBottom;
    private Animation toBottom;

    private Animation fromRight;
    private Animation toRight;

    private ProgressBar userexp;
    private TextView playerIGN;
    private TextView playerLevel;
    private ImageView symbol;

    private LocationCallback locationCallback;
    private LocationRequest locReq;
    private FusedLocationProviderClient fusedLocationClient;

    private int color;

    private GoogleMap mMap;
    private Marker playerPos;
    private Marker playerPosBase;
    private Circle circle;

    private Player player;
    private LatLng loc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);


        parentActivity = (MainGameActivity) getActivity();

        fromBottom = AnimationUtils.loadAnimation(this.getActivity(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this.getActivity(), R.anim.to_bottom_anim);
        fromRight = AnimationUtils.loadAnimation(this.getActivity(), R.anim.from_right_anim);
        toRight = AnimationUtils.loadAnimation(this.getActivity(), R.anim.to_right_anim);

        menu = view.findViewById(R.id.menuButton);
        inventory = view.findViewById(R.id.inventoryButton);
        armymenu = view.findViewById(R.id.armyMenuButton);
        profile = view.findViewById(R.id.profileButton);
        logout = view.findViewById(R.id.logoutButton);
        help = view.findViewById(R.id.helpButton);
        team = view.findViewById(R.id.devteamButton);

        this.userexp = view.findViewById(R.id.userExp);
        this.playerIGN = view.findViewById(R.id.playerIGN);
        this.playerLevel = view.findViewById(R.id.playerLevel);
        this.symbol = view.findViewById(R.id.FactionSymbol);

        menu.setOnClickListener(v -> onAddButtonClicked());
        inventory.setOnClickListener(v -> parentActivity.changeToInventory());

        armymenu.setOnClickListener(v -> parentActivity.changeToArmyMenu());

        profile.setOnClickListener(v -> parentActivity.changeToProfile());

        team.setOnClickListener(v -> parentActivity.changeToAbout());

        help.setOnClickListener(v -> parentActivity.changeToHelp());

        logout.setOnClickListener(v -> logout());

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

    @Override
    public void updateUI(Player p) {
        this.player = p;
        this.playerIGN.setText(p.getInGameName());
        this.playerLevel.setText(String.valueOf(p.getLevel()));
        this.userexp.setProgress(p.getExp());
        this.userexp.setMax(p.getMaxExp());


        color = getResources().getColor(p.getPlayerFaction().primaryColor);
        this.userexp.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        this.symbol.setImageResource(p.getPlayerFaction().symbol);
        this.symbol.setColorFilter(color);

        if (this.playerPos != null) {
            this.playerPos.setIcon(BitmapDescriptorFactory.fromBitmap(getPlayerMarker(R.drawable.marker, 150, 300, p.getPlayerFaction())));
        }

        if (this.playerPosBase != null) {
            this.playerPosBase.setIcon(BitmapDescriptorFactory.fromBitmap(getPlayerMarker(R.drawable.markerbase, 200, 200, p.getPlayerFaction())));
        }

        if(circle != null){
            circle.setStrokeColor(parentActivity.getResources().getColor(p.getPlayerFaction().primaryColor));
        }
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Attention");
        builder.setMessage("You are about to log out. Are you sure you want to leave?");
        builder.setPositiveButton("Ok", (dialog, which) -> parentActivity.logout());
        builder.setNegativeButton("Cancel", (dialog, which) -> {

        });
        AlertDialog d = builder.create();
        d.show();
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setAnimation(Boolean clicked) {
        if (!clicked) {
            profile.startAnimation(fromBottom);
            armymenu.startAnimation(fromBottom);
            inventory.startAnimation(fromBottom);
            logout.startAnimation(fromRight);
            team.startAnimation(fromRight);
            help.startAnimation(fromRight);
        } else {
            profile.startAnimation(toBottom);
            armymenu.startAnimation(toBottom);
            inventory.startAnimation(toBottom);
            logout.startAnimation(toRight);
            team.startAnimation(toRight);
            help.startAnimation(toRight);
        }
    }

    private void setVisibility(Boolean clicked) {
        if (!clicked) {
            profile.setVisibility(View.VISIBLE);
            profile.setClickable(true);
            armymenu.setVisibility(View.VISIBLE);
            armymenu.setClickable(true);
            inventory.setVisibility(View.VISIBLE);
            inventory.setClickable(true);
            logout.setVisibility(View.VISIBLE);
            logout.setClickable(true);

            team.setVisibility(View.VISIBLE);
            team.setClickable(true);
            help.setVisibility(View.VISIBLE);
            help.setClickable(true);
        } else {
            profile.setVisibility(View.INVISIBLE);
            profile.setClickable(false);
            armymenu.setVisibility(View.INVISIBLE);
            armymenu.setClickable(false);
            inventory.setVisibility(View.INVISIBLE);
            inventory.setClickable(false);
            logout.setVisibility(View.INVISIBLE);
            logout.setClickable(false);

            team.setVisibility(View.INVISIBLE);
            team.setClickable(false);
            help.setVisibility(View.INVISIBLE);
            help.setClickable(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }

        this.mMap = googleMap;

        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.mapstyle));
        googleMap.setBuildingsEnabled(false);
        googleMap.setMinZoomPreference(MIN_ZOOM);
        googleMap.setMaxZoomPreference(MAX_ZOOM);
        googleMap.getUiSettings().setCompassEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        //loadPlaces();

        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        this.locReq = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(1000);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {

                        loc = new LatLng(location.getLatitude(), location.getLongitude());

                        createPlayerMarker(loc);

                        CameraPosition cp = new CameraPosition.Builder().target(loc).tilt(TILT).build();
                        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
                    }
                });


        googleMap.setOnMarkerClickListener(marker -> {
            if (!marker.equals(playerPos) && !marker.equals(playerPosBase)) {
                Dialog d = new PlaceDialog(parentActivity, loc, marker.getTitle());
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                d.show();
            }
            return true;
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    loc = new LatLng(location.getLatitude(), location.getLongitude());
                    createPlayerMarker(loc);
                    animateMarker(playerPos, location);
                    animateMarker(playerPosBase, location);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
                }
            }
        };
        fusedLocationClient.requestLocationUpdates(this.locReq, locationCallback, Looper.getMainLooper());
    }

    private void createPlayerMarker(LatLng loc) {
        if (playerPos == null) {
            if (player == null) {
                playerPos = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(getPlayerMarker(R.drawable.marker, 150, 300, null)))
                        .anchor(0.5f, 0.5f)
                        .position(loc));
            } else {
                playerPos = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(getPlayerMarker(R.drawable.marker, 150, 300, player.getPlayerFaction())))
                        .anchor(0.5f, 0.5f)
                        .position(loc));
            }
        }

        if (playerPosBase == null) {
            if (player == null) {
                playerPosBase = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(getPlayerMarker(R.drawable.markerbase, 200, 200, null)))
                        .flat(true)
                        .anchor(0.5f, 0.5f)
                        .position(loc));
            } else {
                playerPosBase = mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromBitmap(getPlayerMarker(R.drawable.markerbase, 200, 200, player.getPlayerFaction())))
                        .flat(true)
                        .anchor(0.5f, 0.5f)
                        .position(loc));
            }
        }

        if(circle == null){
            if (player == null) {
                circle = getMap().addCircle(new CircleOptions()
                        .radius(40)
                        .center(loc)
                        .strokeColor(Color.WHITE));
            }else{
                circle = getMap().addCircle(new CircleOptions()
                        .radius(40)
                        .center(loc)
                        .strokeColor(parentActivity.getResources().getColor(player.getPlayerFaction().primaryColor)));
            }
        }
    }

    private Bitmap getPlayerMarker(int id, int width, int heigt, Faction faction) {
        BitmapDrawable bitmap = (BitmapDrawable) getResources().getDrawable(id);
        Bitmap markerBitmap = Bitmap.createScaledBitmap(bitmap.getBitmap(), width, heigt, false);
        if (faction != null) {
            markerBitmap = changeBitmapColor(markerBitmap, getResources().getColor(faction.primaryColor));
        }
        return markerBitmap;
    }

    private Bitmap changeBitmapColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 0);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, paint);
        return resultBitmap;
    }

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        //final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);

                double lng = t * location.getLongitude() + (1 - t) * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t) * startLatLng.latitude;

                //float rotation = (float) (t * location.getBearing() + (1 - t)*startRotation);

                marker.setPosition(new LatLng(lat, lng));
                circle.setCenter(new LatLng(lat, lng));
                //marker.setRotation(rotation);

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public void loadPlaces() {

        this.LocRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Place p = ds.getValue(Place.class);
                        mMap.addMarker(p.getMarker(getResources()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public GoogleMap getMap() {
        return this.mMap;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}

