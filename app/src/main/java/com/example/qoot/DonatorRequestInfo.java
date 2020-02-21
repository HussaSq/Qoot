package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DonatorRequestInfo extends AppCompatActivity {

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
