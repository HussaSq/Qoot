package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.annotation.Nullable;
import android.widget.AdapterView;
import java.util.zip.Inflater;

import static android.widget.BaseAdapter.*;


public class DonatorRequests extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String UserID,reqID;


    TextView test1, test2 ;

    RelativeLayout Rl;
    LinearLayout req1;
    int Collectionsize=0;
    int max; // max number of request;
    Request[] req;
    Request MAGIC;
    TextView secret;
    LinearLayout parent;
    ListView listView;
     ArrayList<Request> request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_requests);
        Rl = findViewById(R.id.parent);
        req1 = findViewById(R.id.req1);
        listView=findViewById(R.id.list_Request);
        request=new ArrayList<Request>();



    test1 = findViewById(R.id.EventType1);
    test2 = findViewById(R.id.status1);

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

        Query q1 = db.collection("Requests").whereEqualTo("DonatorID",UserID);
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Rl.removeView(req1);
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String State = document.getString("State");
                                String Event = document.getString("TypeOfEvent");
                                reqID = document.getString("RequestID");
                                //test1.setText(Event);
                                //test2.setText(State);
                              //  Toast.makeText(DonatorRequests.this, "It 1"+reqID, Toast.LENGTH_SHORT).show();
                                // NewRequestXML(Event,State,Rl,reqID);
                                MAGIC= new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID);
                                request.add(MAGIC);
                                MyRequestAdapter myRequestAdapter=new MyRequestAdapter(DonatorRequests.this,R.layout.activity_single_request,request);
                                listView.setAdapter(myRequestAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.req1) ;

                                    }
                                });
                            }


                        } else {
                            // Log.d(TAG, "Error getting documents: ", task.getException());
                            // no.setVisibility(View.VISIBLE);
                        }


                    }

                });




    }
/*public void NewRequestXML(String Event, String Time, RelativeLayout whole, String id){

        // this is the bigger request layout ((Root))
         parent = new LinearLayout(this);
        parent.setLayoutParams(new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setClickable(true);


    secret.setText(id);
    Toast.makeText(DonatorRequests.this, "It id: "+id, Toast.LENGTH_SHORT).show();
    secret.setVisibility(View.INVISIBLE);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  OpenDonaterRequestInfo();
                OpenDonaterRequestInfo(parent);

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
    Status.setPadding(30,5,0,0);
    Status.setTextSize(22);
    // حتى هنا
    Status.setText(Time);
    //--------------------------------------------------------------------
    ImageView urgentIcon = new ImageView(this);
    urgentIcon.setLayoutParams(new LinearLayout.LayoutParams(50,50));
    urgentIcon.setPadding(70,8,0,0);
    urgentIcon.setImageResource(R.drawable.urgent);

    parent.addView(EventType);
    parent.addView(Status);
    parent.addView(urgentIcon);
    whole.addView(parent);
    }*/


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
        Intent intent1 = getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(DonatorRequests.this,DonatorRequestInfo.class);
        intent.putExtra("RequestID",reqID);
        intent.putExtra("user", userId);
        startActivity(intent);
    }
}

class Request {

    public String EventType;
    public String Status;
    public String UserID;
    public String ID;

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

class MyRequestAdapter extends BaseAdapter{

    private Context context;
    ArrayList<Request> request;
    int layoutResourseId;

    MyRequestAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }

    public MyRequestAdapter(Context context, int activity_single_request, ArrayList<Request> request) {

        this.request=request;
        this.context=context;
        this.layoutResourseId=activity_single_request;

    }

    @Override
    public int getCount() {
        return request.size();
    }

    @Override
    public Object getItem(int position) {
        return request.get(position).EventType;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_single_request, null);
        TextView eventType=(TextView) view.findViewById(R.id.EventType1);
        TextView status=(TextView) view.findViewById(R.id.status1);
        eventType.setText(request.get(position).EventType);
        status.setText(request.get(position).Status);
        return view;
    }
}





