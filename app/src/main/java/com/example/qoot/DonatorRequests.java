package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DonatorRequests extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String UserID;

    RelativeLayout Rl;
    LinearLayout req1;
    int Collectionsize=0;
    int max; // max number of request;
    Request[] req;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_requests);
        Rl = findViewById(R.id.parent);
        req1 = findViewById(R.id.req1);

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.Req_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                        startActivity(new Intent(getApplicationContext(),DonatorNotifications.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.prfile_don:
                        startActivity(new Intent(getApplicationContext(),DonatorProfile.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.Req_don:
                        return true;

                }
                return false;
            }
        });


        // init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        Rl.removeView(req1);

            Query q1 = db.collection("Requests").whereEqualTo("DonatorID",UserID);
            q1.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String State = document.getString("State");
                                    String Event = document.getString("TypeOfEvent");
                                    NewRequestXML(Event,State,Rl);
                                    max++;

                                    if (max != 5){
                                        String ID = document.getId();
                                        Request R = new Request(Event, State, mAuth.getCurrentUser().getUid(),ID);
                                        req [max]=  R;
                                    }
                                    else {
                                        //   Toast.makeText(this, ")
                                    }


                                }
                            } else {
                                // Log.d(TAG, "Error getting documents: ", task.getException());
                               // no.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }


public void NewRequestXML(String Event, String Time, RelativeLayout whole){

        // this is the bigger request layout ((Root))
        LinearLayout parent = new LinearLayout(this);
        parent.setLayoutParams(new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setClickable(true);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDonaterRequestInfo();
            }
        });
        //android:layout_marginTop="6dp"
    //  android:layout_marginBottom="10dp"
        //add the children of this parent or root
    TextView EventType  =  new TextView(this);
    // التيكست بتتغير حسب الديتابيس
    EventType.setText(Event);
    EventType.setLayoutParams(new LinearLayout.LayoutParams(199,40 ));
    EventType.setPadding(30,20,0,0);
    EventType.setTextSize(22);
    // ---------------------------------------------------------------------
    TextView Status = new TextView(this);
    Status.setLayoutParams(new LinearLayout.LayoutParams(199,40));
    EventType.setPadding(30,5,0,0);
    EventType.setTextSize(22);
    // حتى هنا
    EventType.setText(Time);
    //--------------------------------------------------------------------
    ImageView urgentIcon = new ImageView(this);
    urgentIcon.setLayoutParams(new LinearLayout.LayoutParams(50,50));
    urgentIcon.setPadding(70,8,0,0);
    urgentIcon.setImageResource(R.drawable.urgent);

    parent.addView(EventType);
    parent.addView(Status);
    parent.addView(urgentIcon);
    whole.addView(parent);
    }

    public int CollectionLength(CollectionReference col){
        db.collection("Requests").whereEqualTo("Donator",mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Collectionsize++;
                            }
                        } else {

                        }
                    }
                });
        return Collectionsize;
    }

    public void OpenDonaterRequestInfo(){
        Intent intent = getIntent();
        //Request [] Requestat  = intent.getStringExtra("Requests");
        Intent intent2 = new Intent(DonatorRequests.this,DonatorRequestInfo.class);
        //intent2.putExtra("Requests", Requestat);
        //   startActivity(this,);
    }

    public void OpenRequestForm(View view) {
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        String name = intent1.getStringExtra("Name");

        Intent intent = new Intent(DonatorRequests.this,requestForm.class);
        intent.putExtra("user", userId);
        intent.putExtra("Name", name);
        startActivity(intent);
        // startActivity(new Intent(DonatorRequests.this,requestForm.class));
    }

    public void OpenDonaterRequestInfo(View view) {
        startActivity(new Intent(DonatorRequests.this,DonatorRequestInfo.class));
    }
}

class Request {

    String EventType;
    String Status;
    String UserID;
    String ID;

    public Request(){

    }

    public Request(String type, String stat, String id, String reqID){
        EventType = type;
        Status = stat;
        UserID = id;
        ID = reqID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}

