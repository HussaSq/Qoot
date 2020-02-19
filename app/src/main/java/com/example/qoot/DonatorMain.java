package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DonatorMain extends AppCompatActivity {

    BottomNavigationView BtmView ;


    TextView Username;
    ImageView Photo;

    // eventually we will add comments and ratings as well

    FirebaseAuth mAuth ;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_main);
        BtmView = findViewById(R.id.bottomNavDon);


        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,new DonatorProfileFragment()).commit();
        }

        BtmView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment frag = null;

                switch (menuItem.getItemId()){

                    case R.id.Profile:
                        frag = new DonatorProfileFragment();
                        break;
                    case R.id.Requests:
                        frag = new DonatorNotificationFragment();
                        break;
                    case R.id.Notifications:
                        frag = new DonatorRequestFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, frag).commit();
                return true;
            }
        });
    }
}
