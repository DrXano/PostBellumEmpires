package com.example.postbellumempires;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.adapters.UpgradeMenuAdapter;
import com.example.postbellumempires.gameobjects.GameUnit;
import com.example.postbellumempires.gameobjects.Player;
import com.example.postbellumempires.gameobjects.PlayerArmy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArmyUpgradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArmyUpgradeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference playerRef;
    private RecyclerView upgradeMenu;
    private RecyclerView.LayoutManager layoutManager;
    private ArmyMenuActivity parentActivity;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArmyUpgradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArmyUpgradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArmyUpgradeFragment newInstance(String param1, String param2) {
        ArmyUpgradeFragment fragment = new ArmyUpgradeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_army_upgrade, container, false);

        this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        this.upgradeMenu = v.findViewById(R.id.upgradeMenuRV);
        this.layoutManager = new LinearLayoutManager(getContext());
        this.upgradeMenu.setLayoutManager(this.layoutManager);

        this.parentActivity = (ArmyMenuActivity) getActivity();

        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Player p = snapshot.getValue(Player.class);
                    updateUI(p);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }

    public void updateUI(Player p) {
        PlayerArmy pa = p.getArmy();
        if (pa != null && pa.getUnits() != null) {
            List<GameUnit> units = (List<GameUnit>) pa.getUnits().values();
            GameUnit[] arr = units.toArray(new GameUnit[units.size()]);
            RecyclerView.Adapter upgradeMenuAdapter = new UpgradeMenuAdapter(arr, p, this.layoutManager, getActivity().getResources().getColor(R.color.unavailable), getContext());
            this.upgradeMenu.setAdapter(upgradeMenuAdapter);
        }
    }
}