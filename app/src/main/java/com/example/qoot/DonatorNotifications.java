package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonatorNotifications extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String UserID,reqID;
    Request MAGIC;
    ListView listView;
    ArrayList<Request> request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_notifications);
        setContentView(R.layout.activity_donator_requests);
        listView=findViewById(R.id.list_Request);
        request=new ArrayList<Request>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.notifi_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.Req_don:
                        startActivity(new Intent(getApplicationContext(),DonatorRequests.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.notifi_don:

                        return true;

                    case R.id.prfile_don:
                        startActivity(new Intent(getApplicationContext(),DonatorProfile.class));
                        overridePendingTransition(0,0);
                        return false;

                }
                return false;
            }
        });

        Query q1 = db.collection("Requests").whereEqualTo("DonatorID",UserID).whereEqualTo("State","Accepted");
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String State = document.getString("State");
                                String Event = document.getString("TypeOfEvent");
                                reqID = document.getString("RequestID");
                                String REQTYPE= document.getString("RequestType");

                                MAGIC= new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID, REQTYPE);
                                request.add(MAGIC);
                                MyRequestAdapter myRequestAdapter=new MyRequestAdapter(DonatorNotifications.this,R.layout.activity_single_request,request);
                                listView.setAdapter(myRequestAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Request temp = (Request) parent.getItemAtPosition(position);
                                        Intent in = getIntent();
                                        in.putExtra("RequestID",temp.getID());
                                        Intent intent = new Intent(DonatorNotifications.this,DonatorRequestInfo.class);
                                        intent.putExtra("RequestID",in.getStringExtra("RequestID"));
                                        startActivity(intent);
                                    }
                                });
                            }


                        } else {
                        }
                    }

                });



    }
}
