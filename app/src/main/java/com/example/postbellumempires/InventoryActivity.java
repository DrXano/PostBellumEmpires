package com.example.postbellumempires;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    private LinearLayout Layout;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Toolbar myToolbar = (Toolbar) findViewById(R.id.inventoryToolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Your Inventory");

        this.Layout = findViewById(R.id.inventoryLL);
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
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updateUI(Inventory inventory) {

        if(inventory == null || inventory.getInventory().isEmpty()){
            text.setText(getResources().getString(R.string.emptyInventory));
            this.Layout.removeAllViews();
        }else{
            text.setText("");
            List<Item> items = inventory.getInventory();
            for(Item i : items){
                //ItemViewHolder holder = new ItemViewHolder(this);
                //this.Layout.addView(holder, holder.getLayoutParams());
            }
        }
    }

    public static class ItemViewHolder extends View {
        TextView resourceNameView;
        TextView resourceQuantityView;
        ImageView resourceImageView;

        public ItemViewHolder(Context context){
            super(context);

            /*
            resourceNameView = itemView.findViewById(R.id.resourceName);
            resourceQuantityView = itemView.findViewById(R.id.quantityNumber);
            resourceImageView = itemView.findViewById(R.id.itemImage);
            */
        }
    }
}