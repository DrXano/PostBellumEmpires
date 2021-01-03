package com.example.postbellumempires;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register(View view) {
        String regEmail = ((EditText) findViewById(R.id.RegEmail)).getText().toString();
        String regPass = ((EditText) findViewById(R.id.RegPass)).getText().toString();
        String regConfPass = ((EditText) findViewById(R.id.RegConfPass)).getText().toString();

        if (regEmail.length() == 0) {
            Toast.makeText(this, "Please insert an email", Toast.LENGTH_SHORT).show();
        } else if (regPass.length() == 0) {
            Toast.makeText(this, "Please choose a password", Toast.LENGTH_SHORT).show();
        } else if (regPass.length() < 8) {
            Toast.makeText(this, "Password too short, must have at least 8 characters", Toast.LENGTH_SHORT).show();
        } else if (regConfPass.length() == 0) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
        } else {
            if (regPass.equals(regConfPass)) {
                Intent intent = new Intent(this, pickFactionActivity.class);
                intent.putExtra("email", regEmail);
                intent.putExtra("pass", regPass);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Make sure to confirm correctly your password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void googleRegister(View view) {
        //startActivity(new Intent(this, pickFactionActivity.class));
    }

    public void goToLoginScreen(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}