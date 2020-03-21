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

import de.hdodenhof.circleimageview.CircleImageView;

public class DonatorViewInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView car,name;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_view_info);
        name=findViewById(R.id.UserNameVv);
        car=findViewById(R.id.car);
        circleImageView=findViewById(R.id.colo);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle intent1 = getIntent().getExtras();

        if (intent1 != null) {
            String VolID = (String) intent1.getSerializable("Donators");
            DocumentReference documentReference = db.collection("Donators").document(VolID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>(){

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    name.setText(documentSnapshot.getString("UserName"));

                }
            });



        }
    }

    public void OpenVolunteerNoti(View view) {

            startActivity(new Intent(DonatorViewInfo.this,volunteer_notification.class));

    }
}
