package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.postbellumempires.enums.Faction;
import com.example.postbellumempires.gameobjects.Player;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class NameChooseActivity extends AppCompatActivity {


    private static final String TAG = "NameChooseActivity";

    private String email;
    private String password;
    private Faction faction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_choose);
        this.email = getIntent().getStringExtra("email");
        this.password = getIntent().getStringExtra("pass");
        this.faction = Faction.valueOf(getIntent().getStringExtra("faction"));
    }

    public void choose(View view) {

        String ign = ((EditText) findViewById(R.id.namePickView)).getText().toString();

        if(ign.length() == 0) {
            Toast.makeText(this, "Please choose your InGame name", Toast.LENGTH_SHORT).show();
        }else if(ign.toLowerCase().equals("wisely")){
            Toast.makeText(this, "Ha! Ha! Very funny...", Toast.LENGTH_SHORT).show();
        }else{
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            Player p = new Player(email, ign, faction, 1, 0, 1000);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).setValue(p);
                                startActivity(new Intent(NameChooseActivity.this, MainActivity.class));
                                finish();
                            } else {

                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(NameChooseActivity.this, "Player register failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void cancel(View view){
        Intent intent = new Intent(this, pickFactionActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("pass", password);
        startActivity(intent);
        finish();
    }
}