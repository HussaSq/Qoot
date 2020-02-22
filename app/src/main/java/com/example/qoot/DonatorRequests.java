package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DonatorRequests extends AppCompatActivity {


    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    String UserID;
    String RequestID;

    int Collectionsize=0;
     Request req [] ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_requests);

        // init Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        CollectionReference col = db.collection("Requests");
        Collectionsize = CollectionLength(col);

        // if the user actually has a collection of requests
        if (Collectionsize > 0){

            // since we got the length of collection,
            // initiate the array to navigate easier
            // (عشان نتنقل اسهل في صنع الريكويست حق علياء)
            req = new Request [Collectionsize];
            col.whereEqualTo("Donator",mAuth.getCurrentUser().getUid());
          //  addCollectionToArray(col);



        }

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottomNavDon);
        bottomNavigationView.setSelectedItemId(R.id.Req_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                    startActivity(new Intent(getApplicationContext(),DonatorNotificationFragment.class));
                    overridePendingTransition(0,0);
                    return true;

                    case R.id.Req_don:
                        return true;

                    case R.id.prfile_don:
                        startActivity(new Intent(getApplicationContext(),DonatorProfile.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }
public void NewRequest(){

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
    EventType.setText("EventType");
    //--------------------------------------------------------------------
    ImageView urgentIcon = new ImageView(this);
    urgentIcon.setLayoutParams(new LinearLayout.LayoutParams(50,50));
    urgentIcon.setPadding(70,8,0,0);
    //urgentIcon.set();
  //  android:src="@drawable/urgent" />


}

public int CollectionLength(CollectionReference col){
    db.collection("Requests")
            .whereEqualTo("Donator", mAuth.getCurrentUser().getUid())
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                        //    Log.d(TAG, document.getId() + " => " + document.getData());

                        }
                    } else {

                    }
                }
            });
    return 0;
}

public void OpenDonaterRequestInfo(){
   // String RequestID  = db.collection("Requests").document("");
    startActivity(new Intent(this,DonatorRequestInfo.class));

}


}

class Request {

    String EventType;
    String Status;
    String UserID;
    String RequestID;

    public Request(){

    }

    public Request(String type, String stat, String id){
        EventType = type;
        Status = stat;
        UserID = id;

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
