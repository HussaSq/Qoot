package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
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
    ArrayList<Uri> userIDS;


    //Abeer
    Uri uri;

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
        userIDS=new ArrayList<Uri>();

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
                                String DonatorID=document.getString("DonatorID");
                                if(DonatorID==null)
                                    continue;
                                String ImageName = DonatorID + ".png";
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference mainRef = firebaseStorage.getReference("Images");
                                final File file = new File(getFilesDir(), ImageName);
                                uri = Uri.parse(file.toString());
                                //if(uri!=null)
                                    userIDS.add(uri);
                                MAGIC = new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID, REQTYPE, DonatorName, VolunteerName);
                                request.add(MAGIC);
                                MyNotificationsVolAdapter myRequestAdapter=new MyNotificationsVolAdapter(volunteer_notification.this,R.layout.activity_single_notification,request,reqID,userIDS);
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
    ArrayList<Uri> userIDS;





    MyNotificationsVolAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }

    public MyNotificationsVolAdapter(Context context, int activity_single_notification, ArrayList<Request> request, String reqID,ArrayList<Uri> userIDS) {

        this.request=request;
        this.context=context;
        this.layoutResourseId=activity_single_notification;
        this.reqID=reqID;
        this.userIDS=userIDS;



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

        View view = LayoutInflater.from(context).inflate(R.layout.activity_single_notification, null);
        String volunteer = request.get(position).getVolunteerName() + " ";
        String state = request.get(position).Status;
        String EventType = " The " + request.get(position).EventType + " Request";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString volunteer1 = new SpannableString(volunteer);
        builder.append(volunteer1);

        if (state.equals("Pending")) {
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB8C00")), 0, state.length(), 0);
            builder.append(state1);
        } else if (state.equals("Accepted")) {
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), 0, state.length(), 0);
            builder.append(state1);
        } else if (state.equals("Cancelled")) {
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#BF360C")), 0, state.length(), 0);
            builder.append(state1);
        }else if(state.equals("Delivered")){
            SpannableString state1 = new SpannableString(state);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#0392cf")), 0, state.length(), 0);
            builder.append(state1);
        }

        SpannableString EventType1 = new SpannableString(EventType);

        builder.append(EventType1);
        TextView volunteerName = (TextView) view.findViewById(R.id.requests);
        Uri Uri2=userIDS.get(position);
        volunteerName.setText(builder, TextView.BufferType.SPANNABLE);
        CircleImageView circleImageView=(CircleImageView) view.findViewById(R.id.colo1);
        if(Uri2!=null)
        circleImageView.setImageURI(Uri2);

        return view;
    }

}
