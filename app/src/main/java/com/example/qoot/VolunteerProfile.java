package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class VolunteerProfile extends AppCompatActivity {


    private TextView Username;
    private ImageView Photo;

    // eventually we will add comments and ratings as well

    FirebaseAuth mAuth ;
    FirebaseFirestore fstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);

        Username = findViewById(R.id.UserNameV);
        Photo = findViewById(R.id.UserImage);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        String Uid = mAuth.getCurrentUser().getUid();
        DocumentReference docRef = fstore.collection("users").document(Uid);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Username.setText(documentSnapshot.getString("username"));
            }
        });
    }

    public void OpenEditProfilePage(View view){
        startActivity(new Intent(VolunteerProfile.this,EditVolunteerProfile.class));
    }

    public void OpenLogOut(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(VolunteerProfile.this,LogIn.class));
    }
}
