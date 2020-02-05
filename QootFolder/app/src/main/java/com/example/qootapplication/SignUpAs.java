package com.example.qootapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class SignUpAs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_as);
    }

    public void OpenSignInPage(View view) {
        startActivity(new Intent(SignUpAs.this,LogIn.class));
    }

    public void OpenSignupDonatorPage(View view) {
        startActivity(new Intent(SignUpAs.this,RegisterAsDonator.class));
    }
}
