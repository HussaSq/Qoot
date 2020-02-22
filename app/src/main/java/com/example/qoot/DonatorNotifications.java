package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DonatorNotifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_notifications);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.Req_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.Req_don:
                        startActivity(new Intent(getApplicationContext(),DonatorRequests.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notifi_don:

                        return true;

                    case R.id.prfile_don:
                        startActivity(new Intent(getApplicationContext(),DonatorProfile.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }
}
