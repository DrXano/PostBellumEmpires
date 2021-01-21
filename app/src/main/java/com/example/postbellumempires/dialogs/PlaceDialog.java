package com.example.postbellumempires.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.MainGameActivity;
import com.example.postbellumempires.R;
import com.example.postbellumempires.adapters.PlaceArmyAdapter;
import com.example.postbellumempires.enums.GameResource;
import com.example.postbellumempires.enums.Structure;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Place;
import com.example.postbellumempires.gameobjects.PlaceArmy;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.utils.CollectionCooldown;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlaceDialog extends Dialog implements View.OnClickListener {

    private static final float LIMIT = 40;

    private final MainGameActivity parentActivity;
    private final DatabaseReference PlaceRef;
    public TextView PlaceName;
    public TextView OwnerFaction;
    public TextView Owner;
    public TextView Reward;
    public TextView currcap;
    public TextView maxcap;
    public ImageView symbol;
    public ImageButton cancel;
    public Button action;
    public Button collect;
    public RecyclerView armyView;
    public ImageButton struct1button;
    public ImageButton struct2button;
    public ImageButton struct3button;
    public ImageButton struct4button;
    Context context;
    private Place place;
    private LatLng loc;

    public PlaceDialog(@NonNull Context context, LatLng loc, String id) {
        super(context, android.R.style.Theme_Black_NoTitleBar);
        this.context = context;
        this.loc = loc;
        this.parentActivity = (MainGameActivity) context;
        this.PlaceRef = FirebaseDatabase.getInstance().getReference("places").child(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_place);
        PlaceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Place p = snapshot.getValue(Place.class);
                    updateUI(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUI(Place p) {
        Player player = parentActivity.getPlayer();
        this.place = p;
        action = findViewById(R.id.actionButton);
        collect = findViewById(R.id.collectionButton);
        cancel = findViewById(R.id.cancelButton);
        PlaceName = findViewById(R.id.placeTitle);
        OwnerFaction = findViewById(R.id.factionInPlace);
        Owner = findViewById(R.id.ownerInPlace);
        Reward = findViewById(R.id.ResourceReward);
        currcap = findViewById(R.id.currCap);
        maxcap = findViewById(R.id.maxcap);
        symbol = findViewById(R.id.ownerFactionSymbol);
        armyView = findViewById(R.id.placeArmyRV);
        struct1button = findViewById(R.id.struct1button);
        struct2button = findViewById(R.id.struct2button);
        struct3button = findViewById(R.id.struct3button);
        struct4button = findViewById(R.id.struct4button);
        armyView.setLayoutManager(new LinearLayoutManager(getContext()));

        PlaceArmy army = p.getArmy();

        if (army != null && army.getUnits() != null && !army.getUnits().isEmpty()) {
            this.armyView.setAdapter(new PlaceArmyAdapter(army.getUnitsArray()));
        } else {
            this.armyView.setAdapter(new PlaceArmyAdapter(new GameUnit[]{}));
        }

        PlaceName.setText(p.getName());

        int color;
        if (p.getFaction() != null) {
            color = getContext().getResources().getColor(p.getFaction().primaryColor);
            this.symbol.setImageResource(p.getFaction().symbol);
            this.symbol.setColorFilter(color);
        } else {
            this.symbol.setImageDrawable(null);
        }

        String owner = p.getOwner();
        String ownerFaction = p.getOwnerFaction();
        Reward.setText(p.getResourceRewardType().name);

        currcap.setText(String.valueOf(p.getCapacity()));
        maxcap.setText(String.valueOf(p.getMaxCapacity()));

        Owner.setText((owner == null) ? "---" : owner);
        OwnerFaction.setText((ownerFaction == null) ? "---" : ownerFaction);
        boolean friendly = p.isFriendly(player);

        if (friendly) {
            action.setText("Deploy Troops");
            collect.setText("Collect");
        } else {
            action.setText("Attack");
            collect.setText("Steal");
        }

        struct1button.setImageResource(p.getEStruct1().imageId);
        struct2button.setImageResource(p.getEStruct2().imageId);
        struct3button.setImageResource(p.getEStruct3().imageId);
        struct4button.setImageResource(p.getEStruct4().imageId);

        action.setOnClickListener(this);
        collect.setOnClickListener(this);
        cancel.setOnClickListener(this);
        struct1button.setOnClickListener(this);
        struct2button.setOnClickListener(this);
        struct3button.setOnClickListener(this);
        struct4button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Player player = parentActivity.getPlayer();
        switch (v.getId()) {
            case R.id.actionButton:
                if (isNear()) {
                    if (!this.place.getUnderAttack()) {
                        if (!player.getArmy().isEmpty()) {
                            Dialog d = new ActionDialog(context, this.place, player, this.parentActivity);
                            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                            d.show();
                        } else {
                            Toast.makeText(context, "No army available, train more units", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.collectionButton:
                if (!isNear()) {
                    Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                } else if (this.place.getUnderAttack()) {
                    Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                } else if (CollectionCooldown.getInstance().isCoolDownOn(this.place.getId())) {
                    Toast.makeText(context, "You have collected resources from this place recently. Please come back later", Toast.LENGTH_SHORT).show();
                } else {
                    GameResource res = this.place.getResourceRewardType();
                    Item reward = res.getReward(this.place.multiplier(player));
                    player.addItem(reward.getResourceItem(), reward.getQuantity());
                    player.updatePlayer();
                    Toast.makeText(context, reward.getQuantity() + " x " + reward.getResourceItem().name + " acquired", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                /*
                if (isNear()) {
                    if (!this.place.getUnderAttack()) {
                        GameResource res = this.place.getResourceRewardType();
                        Item reward = res.getReward(this.place.multiplier(player));
                        player.addItem(reward.getResourceItem(), reward.getQuantity());
                        player.updatePlayer();
                        Toast.makeText(context, reward.getQuantity() + " x " + reward.getResourceItem().name + " acquired", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.struct1button:
                if (!this.place.getUnderAttack()) {
                    if (this.place.getEStruct1().equals(Structure.NONE)) {
                        if (this.place.isOccupied() && this.place.isFriendly(player)) {
                            if (isNear()) {
                                Dialog d = new StructureDialog(context, 1, this.place, player, getContext().getResources().getColor(R.color.unavailable));
                                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                                d.show();
                            } else {
                                Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.struct2button:
                if (!this.place.getUnderAttack()) {
                    if (this.place.getEStruct2().equals(Structure.NONE)) {
                        if (this.place.isOccupied() && this.place.isFriendly(player)) {
                            if (isNear()) {
                                Dialog d = new StructureDialog(context, 2, this.place, player, getContext().getResources().getColor(R.color.unavailable));
                                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                                d.show();
                            } else {
                                Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.struct3button:
                if (!this.place.getUnderAttack()) {
                    if (this.place.getEStruct3().equals(Structure.NONE)) {
                        if (this.place.isOccupied() && this.place.isFriendly(player)) {
                            if (isNear()) {
                                Dialog d = new StructureDialog(context, 3, this.place, player, getContext().getResources().getColor(R.color.unavailable));
                                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                                d.show();
                            } else {
                                Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.struct4button:
                if (!this.place.getUnderAttack()) {
                    if (this.place.getEStruct4().equals(Structure.NONE)) {
                        if (this.place.isOccupied() && this.place.isFriendly(player)) {
                            if (isNear()) {
                                Dialog d = new StructureDialog(context, 4, this.place, player, getContext().getResources().getColor(R.color.unavailable));
                                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 0, 0, 0)));
                                d.show();
                            } else {
                                Toast.makeText(context, "You are too far from this place", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(context, "This place is underAttack", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private boolean isNear() {
        LatLng placeLoc = place.getLatLng();
        Location l1 = new Location("");
        l1.setLatitude(loc.latitude);
        l1.setLongitude(loc.longitude);

        Location l2 = new Location("");
        l2.setLatitude(placeLoc.latitude);
        l2.setLongitude(placeLoc.longitude);

        float distance = l1.distanceTo(l2);
        return distance <= LIMIT;
    }
}
