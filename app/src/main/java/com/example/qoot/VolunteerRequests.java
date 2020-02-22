package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VolunteerRequests extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LinearLayout req1 ;
    RelativeLayout whole;
    String VolunteerID;
    TextView no;

    /*

                                فولنتير سوا اكسبت



     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);
            whole = findViewById(R.id.Parent);
            req1 = findViewById(R.id.req1);
            whole.removeView(req1);
            no = findViewById(R.id.NO);

            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            VolunteerID = mAuth.getCurrentUser().getUid();

        Query q1 = db.collection("Requests").whereEqualTo("VolnteerID",VolunteerID);

        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String State = document.getString("State");
                                String Event = document.getString("TypeOfEvent");
                                NewRequestXML(Event,State);
                            }
                        } else {
                           // Log.d(TAG, "Error getting documents: ", task.getException());
                           no.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void NewRequestXML(String Event, String status){

        // this is the bigger request layout ((Root))
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setClickable(true);
        //add the children of this parent or root
        TextView EventType  =  new TextView(this);
        EventType.setText(Event);
        EventType.setLayoutParams(new LinearLayout.LayoutParams(199,40 ));
        EventType.setPadding(30,20,0,0);
        EventType.setTextSize(22);
        // ---------------------------------------------------------------------
        TextView Status = new TextView(this);
        Status.setLayoutParams(new LinearLayout.LayoutParams(199,40));
        EventType.setPadding(30,5,0,0);
        EventType.setTextSize(22);
        EventType.setText(status);
        //--------------------------------------------------------------------
        ImageView urgentIcon = new ImageView(this);
        urgentIcon.setLayoutParams(new LinearLayout.LayoutParams(50,50));
        urgentIcon.setPadding(70,8,0,0);
        urgentIcon.setImageResource(R.drawable.urgent);

        parent.addView(EventType);
        parent.addView(Status);
        parent.addView(urgentIcon);
    }

    public void OpenVolunteerRequestInfo(View view) {
        startActivity(new Intent(VolunteerRequests.this,VolunteerRequestInfo.class));
    }

    public void OpenAllRequests(View view) {
        startActivity(new Intent(VolunteerRequests.this,AllRequests.class));
    }
}
