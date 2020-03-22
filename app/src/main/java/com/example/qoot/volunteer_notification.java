package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class volunteer_notification extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String UserID,reqID;
    Request MAGIC;
    ListView listViewNoti;
    ListView listViewNoti2;
    ArrayList<Request> request;
    Review review;
    ArrayList<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_notification);
        listViewNoti=findViewById(R.id.list_Requestnoti);
        request=new ArrayList<Request>();
        reviewList=new ArrayList<Review>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();

        //BottomNavigationViewStart
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_vol);
        bottomNavigationView.setSelectedItemId(R.id.notifi_vol);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.prfile_vol:
                        startActivity(new Intent(getApplicationContext(),VolunteerProfile.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.notifi_vol:
                        return true;

                    case R.id.Req_vol:
                        startActivity(new Intent(getApplicationContext(),VolunteerRequests.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.browse_vol:
                        startActivity(new Intent(getApplicationContext(),AllRequests.class));
                        overridePendingTransition(0,0);
                        return false;


                }
                return false;
            }
        });

        //BottomNavigationViewEnd

        Query q1 = db.collection("Requests").whereEqualTo("VolnteerID",UserID).whereEqualTo("State", "Cancelled");
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

                                MAGIC = new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID, REQTYPE, DonatorName, VolunteerName);
                                request.add(MAGIC);
                                MyNotificationsVolAdapter myRequestAdapter=new MyNotificationsVolAdapter(volunteer_notification.this,R.layout.activity_single_notification,request,reqID);
                                listViewNoti.setAdapter(myRequestAdapter);
                                listViewNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final Request temp = (Request) parent.getItemAtPosition(position);
                                        DocumentReference VolRef=db.collection("Requests").document(temp.getID());
                                        VolRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String VolID = documentSnapshot.getString("DonatorID");
                                                if(!VolID.equals("--")) {
                                                    Intent in = getIntent();
                                                    //Toast.makeText(volunteer_notification.this, "THE ID "+VolID, Toast.LENGTH_SHORT).show();
                                                    in.putExtra("Donators", VolID);
                                                    Intent intent = new Intent(volunteer_notification.this, DonatorViewInfo.class);
                                                    intent.putExtra("Donators", in.getStringExtra("Donators"));
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

class MyNotificationsVolAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Request> request;
    ArrayList<Review> reviews;
    int layoutResourseId;
    String reqID;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String VolunteerName;
    String UserID;
    String type;




    MyNotificationsVolAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }

    public MyNotificationsVolAdapter(Context context, int activity_single_notification, ArrayList<Request> request, String reqID) {

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

    public int getViewTypeCount(){
        return 2;
    }

    public int getItemViewType(int position){
        if(request!=null)
            return 0;
        else return 1;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
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


        View view = LayoutInflater.from(context).inflate(R.layout.activity_single_notification, null);
        String volunteer = request.get(position).getVolunteerName() + " ";
        String state = request.get(position).Status;
        String EventType = " The " + request.get(position).EventType + " Request";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString volunteer1 = new SpannableString(volunteer);

        //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
        // volunteer1.setSpan(new ForegroundColorSpan(Color.RED),0,volunteer.length(), 0);
        builder.append(volunteer1);

        if (state.equals("Pending")) {
            //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB8C00")), 0, state.length(), 0);
            builder.append(state1);
        } else if (state.equals("Accepted")) {
            // ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#4CAF50"));
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), 0, state.length(), 0);
            builder.append(state1);
        } else if (state.equals("Cancelled")) {
            //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#BF360C"));
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#BF360C")), 0, state.length(), 0);
            builder.append(state1);
        }else if(state.equals("Delivered")){
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#0392cf")), 0, state.length(), 0);
            builder.append(state1);
        }
        //ForegroundColorSpan foregroundColorSpan2=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
        //state1.setSpan(new ForegroundColorSpan(Color.YELLOW),0,state.length(),0);
        //builder.append(state1);
        SpannableString EventType1 = new SpannableString(EventType);
        //ForegroundColorSpan foregroundColorSpan3=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
        //EventType1.setSpan(new ForegroundColorSpan(Color.BLUE),0,EventType.length(), 0);
        builder.append(EventType1);
        //String textbox=volunteer2+" "+state+" Your "+EventType+" Request";
        //  SpannableString spannableString=new SpannableString(textbox);
        TextView volunteerName = (TextView) view.findViewById(R.id.requests);

        volunteerName.setText(builder, TextView.BufferType.SPANNABLE);
        CircleImageView circleImageView=(CircleImageView) view.findViewById(R.id.colo);

        return view;
    }

}
