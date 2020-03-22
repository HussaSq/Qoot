package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

import javax.annotation.Nullable;

public class VolunteerRequestInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView type,guests, location, date, time,notes, DonName,state;
    Button Acceptbtn,cancel;
    String userID;
    LinearLayout noteLay;
    String VolunteerName;
    CheckBox checkDelivered;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Bundle intent1;
    ImageView chat;
    String ABEER;
    String ABEER2;
    String dateCheck,currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_request_info);

        type = findViewById(R.id.FoodType);
        state = findViewById(R.id.requesrStatus);
        guests = findViewById(R.id.numberOfGuest);
        location = findViewById(R.id.location);
        date = findViewById(R.id.Date);
        time = findViewById(R.id.pickUpTime);
        notes = findViewById(R.id.note);
        DonName = findViewById(R.id.volname);
        Acceptbtn = findViewById(R.id.offers);
        noteLay = (LinearLayout) findViewById(R.id.linearLayout4);
        checkDelivered = (CheckBox)findViewById(R.id.checkDel);
        chat = findViewById(R.id.ChatIcon);
        cancel =(Button)findViewById(R.id.cancel) ;

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();
        userID =mAuth.getCurrentUser().getUid();


        if (intent1 != null){
            String ReqIDDD = (String) intent1.getSerializable("RequestID");
            ABEER =(String) intent1.getSerializable("RequestID");
            ABEER2 = (String) intent1.getSerializable("Where");
                // String ReqIDDD = intent1.getStringExtra("RequestID");
                DocumentReference documentReference = db.collection("Requests").document(ReqIDDD);
                documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        type.setText(documentSnapshot.getString("TypeOfEvent"));
                        String ss = (documentSnapshot.getString("State"));
                        SpannableString spannableString=new SpannableString(ss);
                        if(ss.equals("Pending")){
                            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
                            spannableString.setSpan(foregroundColorSpan,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            state.setText(spannableString);
                        }
                        else if(ss.equals("Accepted")){
                            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#4CAF50"));
                            spannableString.setSpan(foregroundColorSpan,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            state.setText(spannableString);
                        }
                        else if(ss.equals("Cancelled")){
                            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#BF360C"));
                            spannableString.setSpan(foregroundColorSpan,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            state.setText(spannableString);
                        }
                        else if(ss.equals("Delivered")){
                            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#0392cf"));
                            spannableString.setSpan(foregroundColorSpan,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            state.setText(spannableString);
                        }

                        guests.setText(documentSnapshot.getString("NumberOfGuests"));
                        location.setText(documentSnapshot.getString("Location"));
                        date.setText(documentSnapshot.getString("Date"));
                        time.setText(documentSnapshot.getString("Time"));

                        String empty = "";
                        if((documentSnapshot.getString("Note"))== empty){
                            noteLay.setVisibility(View.GONE);}
                        else{
                            notes.setText(documentSnapshot.getString("Note"));}
                        DonName.setText(documentSnapshot.getString("DonatorName"));
                        //cancel
                        // checkDate
                        Calendar now = Calendar.getInstance();
                        dateCheck = date.getText().toString();
                        currentDate =(now.get(Calendar.MONTH) + 1)
                                + "/"
                                + now.get(Calendar.DATE)
                                + "/"
                                + now.get(Calendar.YEAR);

                        if(ss.equals("Accepted"))
                            if(!dateCheck.equals(currentDate))
                            cancel.setVisibility(View.VISIBLE);

                        // to display pop up
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(VolunteerRequestInfo.this,VolunteerCancel.class);
                                if(intent1 != null)
                                    i.putExtra("RequestID",(String) intent1.getSerializable("RequestID"));

                                startActivity(i);
                            }
                        });
                        //end of cancel
                        // Delivered checkbox..
                        checkDelivered.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                                if(isChecked) {
                                    checkDelivered.setEnabled(false);
                                    String ReqIDDD = (String) intent1.getSerializable("RequestID");
                                    DocumentReference documentReference2 = db.collection("Requests").document(ReqIDDD);
                                    documentReference2.update("State","Delivered").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                           //Toast.makeText( VolunteerRequestInfo.this,"updated successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Intent pop = new Intent(VolunteerRequestInfo.this, PopReview.class);
                                    if(intent1!= null)
                                        pop.putExtra("RequestID",(String) intent1.getSerializable("RequestID"));

                                    startActivity(pop);
                                }else{
                                    // nothing
                                }
                            }
                        });



                        switch (documentSnapshot.getString("State")){
                            case "Delivered":
                                chat.setVisibility(View.VISIBLE);
                                checkDelivered.setVisibility(View.GONE);
                                Acceptbtn.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);
                                break;
                            case "Pending":
                                chat.setVisibility(View.GONE);
                                checkDelivered.setVisibility(View.GONE);
                                Acceptbtn.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.GONE);
                                break;
                            case "Accepted":
                                chat.setVisibility(View.VISIBLE);
                                checkDelivered.setVisibility(View.VISIBLE);
                                Acceptbtn.setVisibility(View.GONE);
                                cancel.setVisibility(View.VISIBLE);
                                break;
                            case"Cancelled":
                                chat.setVisibility(View.GONE);
                                checkDelivered.setVisibility(View.GONE);
                                Acceptbtn.setVisibility(View.GONE);
                                cancel.setVisibility(View.GONE);
                                break;
                        }


                        if (documentSnapshot.getString("State").equals("Delivered"))
                        {
                            checkDelivered.setVisibility(View.GONE);}


                        if (documentSnapshot.getString("State").equals("Pending"))
                        {
                            checkDelivered.setVisibility(View.GONE);
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

                                DocumentReference VolRef=db.collection("Volunteers").document(userID);
                                VolRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        VolunteerName=documentSnapshot.getString("UserName") ;
                                        documentReference.update("VolnteerName", VolunteerName).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }

                                        });
                                    }
                                });

                                documentReference.update("VolnteerID", userID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                       // Toast.makeText(VolunteerRequestInfo.this, "changed vol id", Toast.LENGTH_SHORT).show();
                                        Intent i2 = new Intent(VolunteerRequestInfo.this, VolunteerRequests.class);
                                        startActivity(i2);
                                    }
                                });




                            }// end of accept button
                        });
                    }
                    }
                });
    }
    }
    public void OpenVolunteerRequests(View view) {
        switch (ABEER2){
            case"Requests":
                Toast.makeText(VolunteerRequestInfo.this, "REQUESTS"+ABEER2, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VolunteerRequestInfo.this, VolunteerRequests.class);
                startActivity(intent);
                break;
            case"tab3":
                Toast.makeText(VolunteerRequestInfo.this, "tab3", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(VolunteerRequestInfo.this, AllRequests.class);
                startActivity(intent2);
                break;
            case"tab4":
                Intent intent3 = new Intent(VolunteerRequestInfo.this, AllRequests.class);
                startActivity(intent3);
                ;
                break;
        }
    }
    public void OpenAttachment(View view){
        Intent in = getIntent();
        in.putExtra("RequestID",ABEER);
        in.putExtra("Who","V");
        Intent intent = new Intent(VolunteerRequestInfo.this, AttachmentPicture.class);
        intent.putExtra("RequestID", in.getStringExtra("RequestID"));
        intent.putExtra("Who", in.getStringExtra("Who"));
        startActivity(intent);
    }
}
