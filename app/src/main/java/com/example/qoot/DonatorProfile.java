package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class DonatorProfile extends AppCompatActivity {

    private TextView Username;
    private ImageView Photo;

    // eventually we will add comments and ratings as well

    FirebaseAuth mAuth ;
    FirebaseFirestore fstore;
    DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_profile);

        Username = findViewById(R.id.UserNameV);
        Photo = findViewById(R.id.UserImage);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        String Uid = mAuth.getCurrentUser().getUid();
        docRef = fstore.collection("users").document(Uid);
        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               // Username.setText(documentSnapshot.getString("username"));
            }
        });

    }
    public void OpenEditProfilePage(View view){
        startActivity(new Intent(DonatorProfile.this,EditDonatorProfile.class));
    }

    public void OpenLogOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(DonatorProfile.this, "log out Was Successful!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(DonatorProfile.this,LogIn.class));
    }
}