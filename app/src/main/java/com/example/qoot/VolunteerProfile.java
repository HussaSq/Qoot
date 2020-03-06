package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class VolunteerProfile extends AppCompatActivity {


    private TextView Username;
    private ImageView Photo;
    FirebaseAuth mAuth ;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_vol);
        bottomNavigationView.setSelectedItemId(R.id.prfile_vol);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_vol:
                        startActivity(new Intent(getApplicationContext(),volunteer_notification.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.prfile_vol:
                        return true;

                    case R.id.Req_vol:
                        startActivity(new Intent(getApplicationContext(),VolunteerRequests.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.browse_vol:
                        startActivity(new Intent(getApplicationContext(),AllRequests.class));
                        overridePendingTransition(0,0);
                        return false;


                }
                return false;
            }
        });
        Username = findViewById(R.id.UserNameV);
        Photo = findViewById(R.id.UserImage);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userId=mAuth.getCurrentUser().getUid();
        DocumentReference documentReference =db.collection("Volunteers").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Username.setText(documentSnapshot.getString("UserName"));
            }
        });

        DocumentReference documentReference1 =db.collection("profilePicture").document(userId);
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String uri = documentSnapshot.getString("link");
                // Picasso.get().load(uri).into(Photo);
                // Photo.setImageURI(uri);
                Toast.makeText(VolunteerProfile.this," Link "+uri,Toast.LENGTH_LONG).show();
                //اعتقد المفروض جوا الload نحط اوبجت من نوع Uri
                // الحين هو سترينق
                Picasso.with(VolunteerProfile.this).load(uri).into(Photo);
                //Picasso.get().(DonatorProfile.this).load(uri).into(Photo);


            }
        });

    }

    public void OpenEditProfilePage(View view){
        startActivity(new Intent(VolunteerProfile.this,EditVolunteerProfile.class));
    }

    public void OpenLogOut(View view){
       // FirebaseAuth.getInstance().signOut();
        Toast.makeText(VolunteerProfile.this, "log out Was Successful!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(VolunteerProfile.this,LogIn.class));
    }
}
