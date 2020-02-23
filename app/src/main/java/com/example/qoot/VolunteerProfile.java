package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class VolunteerProfile extends AppCompatActivity {


    private TextView Username,warnM;
    private ImageView Photo,warn;
    public static final String TAG = "VoluntterProfile";

    // eventually we will add comments and ratings as well
    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    FirebaseUser user ;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.prfile_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                        startActivity(new Intent(getApplicationContext(),volunteer_notification.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.prfile_don:
                        return true;

                    case R.id.Req_don:
                        startActivity(new Intent(getApplicationContext(),VolunteerRequests.class));
                        overridePendingTransition(0,0);
                        return false;

                }
                return false;
            }
        });

        Username = findViewById(R.id.UserNameV);
        Photo = findViewById(R.id.UserImage);
        warn = findViewById(R.id.warn);
        warnM = findViewById(R.id.warnMess);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userId=mAuth.getCurrentUser().getUid();
        FirebaseUser user = mAuth.getCurrentUser();
        if(!user.isEmailVerified()){
            warn.setVisibility(View.VISIBLE);
            warnM.setVisibility(View.VISIBLE);

            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(VolunteerProfile.this, "Verification Email Has Been Sent ", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"OnFailure: Email Not Sent");
                }
            });
        }
        DocumentReference documentReference =db.collection("Volunteers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Username.setText(documentSnapshot.getString("UserName"));

            }
        });

    }

    public void OpenEditProfilePage(View view){
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        String name = intent1.getStringExtra("Name");
        startActivity(new Intent(VolunteerProfile.this,EditVolunteerProfile.class));
    }

    public void OpenLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(VolunteerProfile.this, "log out Was Successful!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(VolunteerProfile.this,LogIn.class));
    }
}
