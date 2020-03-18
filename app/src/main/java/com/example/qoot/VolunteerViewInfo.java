package com.example.qoot;

import androidx.annotation.Nullable;
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

public class VolunteerViewInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView car,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_view_info);
        name=findViewById(R.id.UserNameVv);
        car=findViewById(R.id.car);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle intent1 = getIntent().getExtras();

        if (intent1 != null) {
            String VolID = (String) intent1.getSerializable("Volunteers");
            DocumentReference documentReference = db.collection("Volunteers").document(VolID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>(){

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    name.setText(documentSnapshot.getString("UserName"));
                    car.setText(documentSnapshot.getString("Vehicle"));
                }
            });


        }


    }
    public void OpenDonatorNoti(View view) {
        startActivity(new Intent(VolunteerViewInfo.this,DonatorNotifications.class));
    }
}
