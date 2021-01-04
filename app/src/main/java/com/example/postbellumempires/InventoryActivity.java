package com.example.postbellumempires;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.adapters.InventoryAdapter;
import com.example.postbellumempires.gameobjects.Inventory;
import com.example.postbellumempires.gameobjects.Item;
import com.example.postbellumempires.gameobjects.Player;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private DatabaseReference playerRef;
    private RecyclerView Layout;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Toolbar myToolbar = findViewById(R.id.inventoryToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Your Inventory");

        this.Layout = findViewById(R.id.inventoryRV);
        layoutManager = new LinearLayoutManager(this);
        this.Layout.setLayoutManager(layoutManager);

        this.text = findViewById(R.id.inventoryTV);

        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Player p = snapshot.getValue(Player.class);
                    updateUI(p.getInv());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateUI(Inventory inventory) {

        if (inventory == null || inventory.getInventory() == null || inventory.getInventory().isEmpty()) {
            text.setText(getResources().getString(R.string.emptyInventory));
            this.Layout.removeAllViews();
        } else {
            text.setText("");
            List<Item> items = inventory.getInventory();
            Item[] arr = items.toArray(new Item[items.size()]);
            mAdapter = new InventoryAdapter(arr);
            Layout.setAdapter(mAdapter);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView resourceNameView;
        TextView resourceQuantityView;
        ImageView resourceImageView;

        public ItemViewHolder(View v) {
            super(v);
            resourceNameView = itemView.findViewById(R.id.resourceName);
            resourceQuantityView = itemView.findViewById(R.id.quantityNumber);
            resourceImageView = itemView.findViewById(R.id.itemImage);
        }
    }
}