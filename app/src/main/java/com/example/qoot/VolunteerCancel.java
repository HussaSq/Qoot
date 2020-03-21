package com.example.qoot;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class VolunteerCancel extends Activity {
    Button yes,no;
    Bundle intent1;
    String ReqIDDD;
    FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_pop_up);
// written by the queen
        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // curve
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-40;
        getWindow().setAttributes(params);

        getWindow().setLayout((int)(width*.8),(int)(height*.5));

        //set button;
        yes = (Button)findViewById(R.id.yes);
        no=(Button)findViewById(R.id.no);
        db = FirebaseFirestore.getInstance();
        intent1 = getIntent().getExtras();

        // if user pressed no
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent1 != null) {
                    ReqIDDD = (String) intent1.getSerializable("RequestID");
                }
                Intent i = new Intent(VolunteerCancel.this,VolunteerRequestInfo.class);
                i.putExtra("RequestID",ReqIDDD);
                startActivity(i);

            }
        });
        // if user pressed yes

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent1 != null) {
                    ReqIDDD = (String) intent1.getSerializable("RequestID");
                    // DO NOT FORGET TO ADD IF TO CHECK STATE IF IT PENDING OR ACCEPTED.
                    DocumentReference documentReference =db.collection("Requests").document(ReqIDDD);
                    documentReference.update("State","Pending").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                             //  Toast.makeText( VolunteerCancel.this,"canceeled succes",Toast.LENGTH_SHORT).show();

                        }
                    });
                    documentReference.update("VolnteerID","--").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                         //   Toast.makeText( VolunteerCancel.this,"canceeled succes",Toast.LENGTH_SHORT).show();

                        }
                    });
                    documentReference.update("VolnteerName","--").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                           // Toast.makeText( VolunteerCancel.this,"canceeled succes",Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                Intent i = new Intent(VolunteerCancel.this,VolunteerRequests.class);
                // i.putExtra("RequestID",ReqIDDD);
                startActivity(i);
            }
        });


    }
}
