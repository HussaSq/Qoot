package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class DonatorProfile extends AppCompatActivity {

     TextView Username;
     ImageView Photo;

    // eventually we will add comments and ratings as well

    FirebaseAuth mAuth ;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_profile);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.Req_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                        startActivity(new Intent(getApplicationContext(),DonatorNotifications.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.prfile_don:
                        return true;

                    case R.id.Req_don:
                        startActivity(new Intent(getApplicationContext(),DonatorRequests.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        Username = findViewById(R.id.UserNameD);
        Photo = findViewById(R.id.UserImage);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userId=mAuth.getCurrentUser().getUid();

        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Username.setText(documentSnapshot.getString("UserName"));

            }
        });

    }
    public void OpenEditProfilePage(View view){
        startActivity(new Intent(DonatorProfile.this,EditDonatorProfile.class));
    }

    public void OpenLogOut(View view) {
        Toast.makeText(DonatorProfile.this, "log out Was Successful!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DonatorProfile.this,LogIn.class);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();

    }

}