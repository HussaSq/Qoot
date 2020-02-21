package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class VolunteerRequestInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_info);
    }

    public void OpenVolunteerRequests(View view) {
        startActivity(new Intent(VolunteerRequestInfo.this,VolunteerRequests.class));
    }
}
