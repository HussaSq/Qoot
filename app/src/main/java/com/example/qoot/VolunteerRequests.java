package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class VolunteerRequests extends AppCompatActivity {

    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    String USerID;
    String RequestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.Req_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                        startActivity(new Intent(getApplicationContext(),volunteer_notification.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.prfile_don:
                        startActivity(new Intent(getApplicationContext(),VolunteerProfile.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.Req_don:
                        return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

       Intent intent=this.getIntent();
        USerID = intent.getStringExtra("user");

        Query q1 = db.collection("Requests").whereEqualTo("VolunteerID",USerID);
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String State = document.getString("State");
                                String Event = document.getString("TypeOfEvent");
                               RequestID = document.getString("RequestID");
                            }
                        } else {

                        }
                    }
                });
    }

    public void NewRequestXML(){

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
        EventType.setText("EventType");
        EventType.setLayoutParams(new LinearLayout.LayoutParams(199,40 ));
        EventType.setPadding(30,20,0,0);
        EventType.setTextSize(22);
        // ---------------------------------------------------------------------
        TextView Status = new TextView(this);
        Status.setLayoutParams(new LinearLayout.LayoutParams(199,40));
        EventType.setPadding(30,5,0,0);
        EventType.setTextSize(22);
        EventType.setText("Status");
        //--------------------------------------------------------------------
        ImageView urgentIcon = new ImageView(this);
        urgentIcon.setLayoutParams(new LinearLayout.LayoutParams(50,50));
        urgentIcon.setPadding(70,8,0,0);
        //urgentIcon.set();
        //  android:src="@drawable/urgent" />


    }


    public void OpenVolunteerRequestInfo(View view) {
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(VolunteerRequests.this,VolunteerRequestInfo.class);
        intent.putExtra("user",userId);
        intent.putExtra("RequestID",RequestID);
        startActivity(intent);
    }

    public void OpenAllRequests(View view) {
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(VolunteerRequests.this,AllRequests.class);
        intent.putExtra("user",userId);
        intent.putExtra("RequestID",RequestID);
        startActivity(intent);
    }
}
