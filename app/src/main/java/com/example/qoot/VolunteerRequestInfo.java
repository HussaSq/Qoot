package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class VolunteerRequestInfo extends AppCompatActivity {



    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView type,guests, location, date, time,notes, DonName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_info);

        type = findViewById(R.id.FoodType);
        guests = findViewById(R.id.numberOfGuest);
        location = findViewById(R.id.location);
        date = findViewById(R.id.Date);
        time = findViewById(R.id.pickUpTime);
        notes = findViewById(R.id.note);
        DonName = findViewById(R.id.volname);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        /*
                            this doesn't work since i don't have an idea
                            on how to get the  exact Request ID but
                            i'm working on it
         */
        DocumentReference documentReference =db.collection("Requests").document(/* هنا ريكويست الايدي*/);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                type.setText(documentSnapshot.getString("TypeOfEvent"));
                guests.setText(documentSnapshot.getString("NumberOfGuests"));
                location.setText(documentSnapshot.getString("Location"));
                date.setText(documentSnapshot.getString("Date"));
                time.setText(documentSnapshot.getString("Time"));
                notes.setText(documentSnapshot.getString("Note"));

                 // this one is kinda wrong since in requests there's no Username/name in requests
               // DonName.setText(documentSnapshot.getString("Donator"));
            }
        });
    }

    public void OpenVolunteerRequests(View view) {
        startActivity(new Intent(VolunteerRequestInfo.this,VolunteerRequests.class));
    }
}
