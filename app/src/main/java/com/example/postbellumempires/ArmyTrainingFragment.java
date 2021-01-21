package com.example.postbellumempires;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.postbellumempires.adapters.CurrentArmyAdapter;
import com.example.postbellumempires.adapters.TrainMenuAdapter;
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
 * Use the {@link ArmyTrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArmyTrainingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference playerRef;
    private RecyclerView currentArmy;
    private RecyclerView trainMenu;
    private TextView currarmyMSG;
    private TextView maxCapacity;
    private TextView currentsize;

    private LinearLayoutManager llm;

    private String mParam1;
    private String mParam2;

    public ArmyTrainingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArmyTrainingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArmyTrainingFragment newInstance(String param1, String param2) {
        ArmyTrainingFragment fragment = new ArmyTrainingFragment();
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
        View v = inflater.inflate(R.layout.fragment_army_training, container, false);

        this.playerRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        this.currentArmy = v.findViewById(R.id.CurrArmyRV);
        this.trainMenu = v.findViewById(R.id.ArmyTrainRV);
        this.currarmyMSG = v.findViewById(R.id.CurrArmyMSG);
        this.maxCapacity = v.findViewById(R.id.maxCap);
        this.currentsize = v.findViewById(R.id.currsize);

        this.llm = new LinearLayoutManager(getActivity());

        this.trainMenu.setLayoutManager(llm);
        this.currentArmy.setLayoutManager(new LinearLayoutManager(getActivity()));

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
        if (this.isResumed()) {
            this.updateCurrentArmy(p);
            this.updateTrainMenu(p);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        playerRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    private void updateCurrentArmy(Player p) {
        PlayerArmy pa = p.getArmy();
        if (pa == null || pa.getUnits() == null || pa.isEmpty()) {
            currarmyMSG.setText("No units trained");
            this.maxCapacity.setText(String.valueOf(p.getMaxCapacity()));
            this.currentsize.setText(String.valueOf(0));
            this.currentArmy.setAdapter(new CurrentArmyAdapter(new GameUnit[]{}));
        } else {
            currarmyMSG.setText("");
            this.maxCapacity.setText(String.valueOf(p.getMaxCapacity()));
            this.currentsize.setText(String.valueOf(pa.getSize()));
            List<GameUnit> units = pa.getAvailableUnits();
            GameUnit[] arr = units.toArray(new GameUnit[units.size()]);
            this.currentArmy.setAdapter(new CurrentArmyAdapter(arr));
        }
    }

    private void updateTrainMenu(Player p) {
        PlayerArmy pa = p.getArmy();
        if (pa != null && pa.getUnits() != null) {
            List<GameUnit> units = pa.getUnitList();
            int pos = llm.findFirstCompletelyVisibleItemPosition();
            TrainMenuAdapter trainMenuAdapter = new TrainMenuAdapter(getActivity().getResources().getColor(R.color.unavailable), getContext());
            trainMenuAdapter.setPlayer(p);
            for (GameUnit gu : units) {
                trainMenuAdapter.addUnit(gu);
                int index = trainMenuAdapter.getItemCount() - 1;
                trainMenuAdapter.notifyItemChanged(index);
            }
            this.trainMenu.setAdapter(trainMenuAdapter);
            llm.scrollToPosition(pos);
        }
    }
}