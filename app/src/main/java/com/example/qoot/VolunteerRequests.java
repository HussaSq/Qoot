package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VolunteerRequests extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);
    }

    public void OpenVolunteerRequestInfo(View view) {
        startActivity(new Intent(VolunteerRequests.this,VolunteerRequestInfo.class));
    }

    public void OpenAllRequests(View view) {
        startActivity(new Intent(VolunteerRequests.this,AllRequests.class));
    }
}
