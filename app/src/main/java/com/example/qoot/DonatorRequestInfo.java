package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class DonatorRequestInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView type,guests, location, date, time,notes, volName;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_request_info);
        type = findViewById(R.id.FoodType);
        guests = findViewById(R.id.numberOfGuest);
        location = findViewById(R.id.location);
        date = findViewById(R.id.Date);
        time = findViewById(R.id.pickUpTime);
        notes = findViewById(R.id.note);
        volName = findViewById(R.id.volname);

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
                volName.setText(documentSnapshot.getString("Volunteer"));

                // عشان نضيف الايكون على حسب الطلب
                switch (documentSnapshot.getString("State"))
                {
                    case"Pending":
                       // AddPendingIcon();
                        break;
                    case "Accepted":
                       // AddAcceptedIcon();
                        break;
                    case "Cancelled":
                        break;
                }

            }
        });
    }

    public void AddPendingIcon(){

    }

    public void AddAcceptedIcon(){

    }
    public void AddCancelledIcon(){

    }

    public void OpenDonatorRequest(View view) {
        startActivity(new Intent(DonatorRequestInfo.this,DonatorRequests.class));
    }

    public void OpenListOffer(View view) {
        startActivity(new Intent(DonatorRequestInfo.this,list_offers.class));
    }
}
