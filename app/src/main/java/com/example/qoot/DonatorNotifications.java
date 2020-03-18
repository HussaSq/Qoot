package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.awt.font.*;
import java.awt.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

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

        //.whereEqualTo("State"," Accepted || Cancelled")

        Query q1 = db.collection("Requests").whereEqualTo("DonatorID",UserID).whereIn("State", Arrays.asList("Accepted","Cancelled"));
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
                                String DonatorName=document.getString("DonatorName");
                                String VolunteerName=document.getString("VolnteerName");
                                String VolunteerID=document.getString("VolnteerID");
                                if(VolunteerID.equals("--"))
                                    continue;
                                if(VolunteerName.equals("--"))
                                    continue;
                                    MAGIC = new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID, REQTYPE, DonatorName, VolunteerName);
                                    request.add(MAGIC);

                                MyNotificationsAdapter myRequestAdapter=new MyNotificationsAdapter(DonatorNotifications.this,R.layout.activity_single_notification,request,reqID);
                                listView.setAdapter(myRequestAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final Request temp = (Request) parent.getItemAtPosition(position);
                                        DocumentReference VolRef=db.collection("Requests").document(temp.getID());
                                        VolRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String VolID = documentSnapshot.getString("VolnteerID");
                                                if(!VolID.equals("--")) {


                                                    Intent in = getIntent();
                                                    in.putExtra("Volunteers", VolID);
                                                    Intent intent = new Intent(DonatorNotifications.this, VolunteerViewInfo.class);
                                                    intent.putExtra("Volunteers", in.getStringExtra("Volunteers"));
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }
                                });
                            }


                        } else {
                        }
                    }

                });





    }
}

class MyNotificationsAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Request> request;
    int layoutResourseId;
    String reqID;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String VolunteerName;
    String UserID;




    MyNotificationsAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }

    public MyNotificationsAdapter(Context context, int activity_single_notification, ArrayList<Request> request, String reqID) {

        this.request=request;
        this.context=context;
        this.layoutResourseId=activity_single_notification;
        this.reqID=reqID;
        //this.VolunteerName=VolunteerName;


    }

    @Override
    public int getCount() {
        return request.size();
    }

    @Override
    public Object getItem(int position) {
        return request.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getreqID() {
        return reqID;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_single_notification, null);
        /*TextView volunteerName=(TextView) view.findViewById(R.id.volunteerName);
        TextView status=(TextView) view.findViewById(R.id.state);
        TextView requestType=(TextView) view.findViewById(R.id.request);

        volunteerName.setText(request.get(position).getVolunteerName());
        requestType.setText("Your "+request.get(position).EventType+" Request");
        String ss=request.get(position).Status;

        SpannableString spannableString=new SpannableString(ss);
        if(ss.equals("Pending")){
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
            spannableString.setSpan(foregroundColorSpan,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            status.setText(spannableString);
        }
        else if(ss.equals("Accepted")){
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#4CAF50"));
            spannableString.setSpan(foregroundColorSpan,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            status.setText(spannableString);
        }
        else if(ss.equals("Cancelled")){
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#BF360C"));
            spannableString.setSpan(foregroundColorSpan,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            status.setText(spannableString);
        }*/
        String volunteer= request.get(position).getVolunteerName()+" ";
        String state=request.get(position).Status;
        String EventType=" Your " +request.get(position).EventType+" Request";
        SpannableStringBuilder builder=new SpannableStringBuilder();
        SpannableString volunteer1=new SpannableString(volunteer);

        //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
       // volunteer1.setSpan(new ForegroundColorSpan(Color.RED),0,volunteer.length(), 0);
        builder.append(volunteer1);

        if(state.equals("Pending")){
            //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
            SpannableString state1=new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB8C00")),0,state.length(),0);
            builder.append(state1);
        }
        else if(state.equals("Accepted")){
           // ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#4CAF50"));
            SpannableString state1=new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")),0,state.length(), 0);
            builder.append(state1);
        }
        else if(state.equals("Cancelled")){
            //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#BF360C"));
            SpannableString state1=new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#BF360C")),0,state.length(), 0);
            builder.append(state1);
        }
        //ForegroundColorSpan foregroundColorSpan2=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
        //state1.setSpan(new ForegroundColorSpan(Color.YELLOW),0,state.length(),0);
        //builder.append(state1);
        SpannableString EventType1=new SpannableString(EventType);
        //ForegroundColorSpan foregroundColorSpan3=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
        //EventType1.setSpan(new ForegroundColorSpan(Color.BLUE),0,EventType.length(), 0);
        builder.append(EventType1);
        //String textbox=volunteer2+" "+state+" Your "+EventType+" Request";
       //  SpannableString spannableString=new SpannableString(textbox);
        TextView volunteerName=(TextView) view.findViewById(R.id.requests);

        volunteerName.setText(builder, TextView.BufferType.SPANNABLE);

        return view;
    }
}
