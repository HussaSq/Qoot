package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String UserID,reqID;
    Request MAGIC;
    ListView listView;
    ArrayList<Request> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_requests);
        listView=findViewById(R.id.list_Request);
        request=new ArrayList<Request>();

        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_vol);
        bottomNavigationView.setSelectedItemId(R.id.Req_vol);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_vol:
                        startActivity(new Intent(getApplicationContext(),volunteer_notification.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.Req_vol:
                        return true;

                    case R.id.prfile_vol:
                        startActivity(new Intent(getApplicationContext(),VolunteerProfile.class));
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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();

        Query q1 = db.collection("Requests").whereEqualTo("VolnteerID",UserID);
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String State = document.getString("State");
                                String Event = document.getString("TypeOfEvent");
                                reqID = document.getString("RequestID");
                                MAGIC= new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID);
                                request.add(MAGIC);
                                MyVolRequestAdapter myRequestAdapter=new MyVolRequestAdapter(VolunteerRequests.this,R.layout.activity_single_request,request);
                                listView.setAdapter(myRequestAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Request temp = (Request) parent.getItemAtPosition(position);
                                        Intent in = getIntent();
                                        in.putExtra("RequestID",temp.getID());
                                        Intent intent = new Intent(VolunteerRequests.this,VolunteerRequestInfo.class);
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
    public void OpenAllRequests(View view) {
        startActivity(new Intent(VolunteerRequests.this,AllRequests.class));
    }
}
class MyVolRequestAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Request> request;
    int layoutResourseId;

    MyVolRequestAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }

    public MyVolRequestAdapter(Context context, int activity_single_request, ArrayList<Request> request) {

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
        return request.get(position);
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
        String ss=request.get(position).Status;
        SpannableString spannableString=new SpannableString(ss);
        if(ss.equals("Pending")){
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.YELLOW);
            spannableString.setSpan(foregroundColorSpan,0,7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            status.setText(spannableString);
        }
        else if(ss.equals("Accepted")){
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.GREEN);
            spannableString.setSpan(foregroundColorSpan,0,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            status.setText(spannableString);
        }
        else if(ss.equals("Cancelled")){
            ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.RED);
            spannableString.setSpan(foregroundColorSpan,0,9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            status.setText(spannableString);
        }


        return view;
    }
}






