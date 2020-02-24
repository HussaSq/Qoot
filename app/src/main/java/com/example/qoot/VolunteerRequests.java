package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.util.ArrayList;

public class VolunteerRequests extends AppCompatActivity {

    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    String USerID;
    String RequestID;

    String userId;
    LinearLayout linearLayou;
    Request MAGIC;
    //حق النكبه
    int count = 1;

    ListView listView;
    ArrayList <Request> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.Req_don);

        listView=findViewById(R.id.list_Request);
        request=new ArrayList<Request>();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent=this.getIntent();
        USerID = intent.getStringExtra("user");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                        Intent i = new Intent(VolunteerRequests.this,volunteer_notification.class);
                       // i.putExtra("user",userId);
                        startActivity(i);
                       // startActivity(new Intent(getApplicationContext(),volunteer_notification.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.prfile_don:
                        Intent i2 = new Intent(VolunteerRequests.this,VolunteerProfile.class);
                       // i2.putExtra("user",userId);
                       startActivity(i2);
                       // startActivity(new Intent(getApplicationContext(),VolunteerProfile.class));
                        overridePendingTransition(0,0);
                        return false;
                    case R.id.Req_don:
                        return true;
                }
                return false;
            }
        });

        Query q1 = db.collection("Requests").whereEqualTo("VolnteerID",USerID);
        //Toast.makeText(VolunteerRequests.this, "out loop " +USerID, Toast.LENGTH_SHORT).show();
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String Event = document.getString("TypeOfEvent");
                                String State = document.getString("State");
                                RequestID = document.getString("RequestID");
                               // Toast.makeText(VolunteerRequests.this, "inside loop " +RequestID, Toast.LENGTH_SHORT).show();

                                // retreive then
                                MAGIC =new Request(Event,State, USerID,RequestID);
                                request.add(MAGIC);
                                MyVolunteerRequestAdapter myRequestAdapter=new MyVolunteerRequestAdapter(VolunteerRequests.this,R.layout.activity_volunteer_single_request,request);
                                listView.setAdapter(myRequestAdapter);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        LinearLayout  linearLayout= (LinearLayout) view.findViewById(R.id.req1) ;

                                    }
                                });
                            }
                        } else {

                        }
                    }
                });
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

    public void OpenVolunteerRequestInfor(View view) {
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(VolunteerRequests.this,VolunteerRequestInfo.class);
        intent.putExtra("user",userId);
        intent.putExtra("RequestID",RequestID);
        startActivity(intent);

    }
}

        class MyVolunteerRequestAdapter extends BaseAdapter {
    private Context context;
    ArrayList <Request> request;
    int layoutResourseId;

    MyVolunteerRequestAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }

    public MyVolunteerRequestAdapter(Context context, int activity_volunteer_single_request, ArrayList<Request> request)
    {
        this.request=request;
        this.context=context;
        this.layoutResourseId=activity_volunteer_single_request;
        int size=getCount();
       // Toast.makeText(context,"size :", Toast.LENGTH_SHORT).show();
    }
    @Override
    public int getCount() {
        return request.size();
    }
    @Override
    public Object getItem(int position) {
        return request.get(position).EventType+"/n"+request.get(position).Status;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_volunteer_single_request, null);
        TextView eventType=(TextView) view.findViewById(R.id.EventType1);
        TextView status=(TextView) view.findViewById(R.id.status1);
        eventType.setText(request.get(position).EventType);
        status.setText(request.get(position).Status);
        return view;
    }
}


/*
                                                خرابيط عبير

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

    public void UglyMethod(LinearLayout l, TextView t , TextView s, String type, String state ){

        l.setVisibility(View.VISIBLE);
        t.setText(type);
        s.setText(state);
    }



 l1 = findViewById(R.id.req1);
            l2 = findViewById(R.id.req2);
            l3 = findViewById(R.id.req3);
            l4 = findViewById(R.id.req4);

            t1 = findViewById(R.id.EventType1);
            t2 = findViewById(R.id.EventType2);
            t3 = findViewById(R.id.EventType3);
            t4 = findViewById(R.id.EventType4);

            s1 = findViewById(R.id.status1);
            s2 = findViewById(R.id.status2);
            s3 = findViewById(R.id.status3);
            s4 = findViewById(R.id.status4);



if (count <= 4) {
                                   // Toast.makeText(VolunteerRequests.this,"Document isnt null u r in", Toast.LENGTH_SHORT).show();
                                    String State = document.getString("State");
                                    String Event = document.getString("TypeOfEvent");
                                    RequestID = document.getString("RequestID");
                                    switch (count){
                                        case 1:
                                            UglyMethod(l1,t1,s1,State,Event);
                                            break;
                                        case 2:
                                            UglyMethod(l2,t2,s2,State,Event);
                                            break;
                                        case 3:
                                            UglyMethod(l3,t3,s3,State,Event);
                                            break;
                                        case 4:
                                            UglyMethod(l4,t4,s4,State,Event);
                                            break;
                                    }
                                    count++;

                                }
                                else {
                                    Toast.makeText(VolunteerRequests.this,"You Cant Accept more than 4", Toast.LENGTH_SHORT).show();
                                }
 */