package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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
    Button Acceptbtn;
    String userID;


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
        Acceptbtn = findViewById(R.id.offers);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Bundle intent1 = getIntent().getExtras();
         userID =mAuth.getCurrentUser().getUid();
        if (intent1 != null){
            String ReqIDDD = (String) intent1.getSerializable("RequestID");



                // String ReqIDDD = intent1.getStringExtra("RequestID");
                // Toast.makeText(DonatorRequestInfo.this, "It" + ReqIDDD, Toast.LENGTH_SHORT).show();
                DocumentReference documentReference = db.collection("Requests").document(ReqIDDD);

                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        type.setText(documentSnapshot.getString("TypeOfEvent"));
                        guests.setText(documentSnapshot.getString("NumberOfGuests"));
                        location.setText(documentSnapshot.getString("Location"));
                        date.setText(documentSnapshot.getString("Date"));
                        time.setText(documentSnapshot.getString("Time"));
                        notes.setText(documentSnapshot.getString("Note"));
                        //volName.setText(documentSnapshot.getString("Volunteer"));
                        Bundle intent1 = getIntent().getExtras();
                        String ReqIDDD = (String) intent1.getSerializable("RequestID");
                        DocumentReference documentReference = db.collection("Requests").document(ReqIDDD);

                        if (documentSnapshot.getString("State").equals("Pending"))
                        {
                            Acceptbtn.setVisibility(View.VISIBLE);
                            Acceptbtn.setOnClickListener(new View.OnClickListener() {
                            Bundle intent1 = getIntent().getExtras();
                            String ReqIDDD = (String) intent1.getSerializable("RequestID");
                            DocumentReference documentReference = db.collection("Requests").document(ReqIDDD);

                            @Override
                            public void onClick(View view) {
                                documentReference.update("State", "Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Toast.makeText(VolunteerRequestInfo.this,"state changed",Toast.LENGTH_SHORT).show();
                                    }

                                });
                                documentReference.update("VolunteerID", userID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(VolunteerRequestInfo.this, "changed vol id", Toast.LENGTH_SHORT).show();
                                        Intent i2 = new Intent(VolunteerRequestInfo.this, VolunteerRequests.class);
                                        startActivity(i2);
                                    }
                                });

                            }// end of accept button


                        });
                    }
                    }
                });






    }//end big if

       // Intent intent1 = getIntent();
      //  final String reqID= intent1.getStringExtra("RequestID");
        //final String userID= intent1.getStringExtra("user");



        //String reqId = intent1.getStringExtra(&quot;RequestID&quot;);
        //String vId= intent1.getStringExtra(&quot;user&quot;);
       // String vName = intent1.getStringExtra(&quot;name&quot;);

/*
       final DocumentReference documentReference = db.collection("Requests").document(reqID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                type.setText(documentSnapshot.getString("TypeOfEvent"));
                guests.setText(documentSnapshot.getString("NumberOfGuests"));
                location.setText(documentSnapshot.getString("Location"));
                date.setText(documentSnapshot.getString("Date"));
                time.setText(documentSnapshot.getString("Time"));
                notes.setText(documentSnapshot.getString("Note"));

                Acceptbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.update("State","Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                               // Toast.makeText(VolunteerRequestInfo.this,"state changed",Toast.LENGTH_SHORT).show();
                            }

                        });
                        documentReference.update("VolunteerID",userID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                              public void onSuccess(Void aVoid) {
                                //Toast.makeText( VolunteerRequestInfo.this,"changed vol id",Toast.LENGTH_SHORT).show();
                          }
                  });

                }); // end of accept button

            }
        });*/

    }


    public void OpenVolunteerRequests(View view) {
        Intent intent1 = getIntent();
       String userId = intent1.getStringExtra("user");
        //String name = intent1.getStringExtra("Name");
        Intent intent = new Intent(VolunteerRequestInfo.this,VolunteerRequests.class);
        intent.putExtra("user", userId);
        //intent.putExtra("Name", name);
        startActivity(intent);
       // startActivity(new Intent(VolunteerRequestInfo.this,VolunteerRequests.class));
    }
}
