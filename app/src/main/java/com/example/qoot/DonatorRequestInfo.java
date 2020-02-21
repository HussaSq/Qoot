package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonatorRequestInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView type,guests, location, date, time,notes, volName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_request_info);
    }

    public void OpenDonatorRequest(View view) {
        startActivity(new Intent(DonatorRequestInfo.this,DonatorRequests.class));
    }

    public void OpenListOffer(View view) {
        startActivity(new Intent(DonatorRequestInfo.this,list_offers.class));
    }
}
