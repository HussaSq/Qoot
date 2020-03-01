package com.example.qoot;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab3 extends Fragment {
    LinearLayout linearLayout;

    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    String USerID;
    String RequestID;
    Request MAGIC;
    ListView listView;
    ArrayList <Request> request;

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }


    public tab3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tab3, container, false);

        listView=view.findViewById(R.id.list_Request);
        request=new ArrayList<Request>();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Intent intent=getActivity().getIntent();
        USerID = intent.getStringExtra("user");


        Query q1 = db.collection("Requests").whereEqualTo("State","Pending");
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String Event = document.getString("TypeOfEvent");
                                String Time = document.getString("Time");
                                RequestID = document.getString("RequestID");
                                MAGIC =new Request(Event,Time, USerID,RequestID);
                                request.add(MAGIC);
                                MyBrowseRequestAdapter myRequestAdapter=new MyBrowseRequestAdapter(getActivity(),R.layout.activity_single_request,request);
                                listView.setAdapter(myRequestAdapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.req1) ;

                                    }
                                });
                            }
                        } else {

                        }
                    }
                });
/*
        linearLayout = (LinearLayout) view.findViewById(R.id.req1);
        linearLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),VolunteerRequestInfo.class));
            }

        });*/
        return view;
    }

    public void CreateXML (){

        // this is the bigger request layout ((Root))
        LinearLayout parent = new LinearLayout(mContext);
        parent.setLayoutParams(new LinearLayout.LayoutParams(160,125));
        //parent.setLeftTopRightBottom(15,6,5,10);
        parent.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(new LinearLayout.LayoutParams(160,125));
        layoutParams.setMargins(30, 20, 30, 0);
        parent.setClickable(true);
        //parent.addView(layoutParams);
       // parent.setBackground("#80E4E2E2");

        TextView event = new TextView(mContext);
        event.setLayoutParams(new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        event.setText("EventType");
        event.setTextSize(22);

        parent.addView(event);

        LinearLayout child = new LinearLayout(mContext);
        child.setLayoutParams(new LinearLayout.LayoutParams(140,49));
        child.setOrientation(LinearLayout.HORIZONTAL);

        parent.addView(child);

        ImageView img = new ImageView(mContext);
        img.setLayoutParams(new LinearLayout.LayoutParams(30,50));
        img.setImageResource(R.drawable.pickuptime);
        parent.addView(img);

        TextView time = new TextView(mContext);
        time.setLayoutParams(new LinearLayout.LayoutParams(68,40));
        time.setText("Time");
        time.setTextSize(20);

        parent.addView(time);

        /*
         android:background="#80E4E2E2"
         android:src="@drawable/pickuptime" />

                <TextView
                    android:id="@+id/time1"
                    android:layout_width="68dp"
                    android:layout_height="40dp"
                    android:layout_marginLeft="10dp"
                    android:layout_marginTop="8dp"
                    android:text="time"
                    android:textSize="20dp" />
            </LinearLayout>
        */
    }

    public void OpenVolunteerRequestInfo(View view) {
        Intent intent1=getActivity().getIntent();
        String userId = intent1.getStringExtra("user");
        Intent intent = new Intent(getActivity(),VolunteerRequestInfo.class);
        intent.putExtra("user",userId);
        intent.putExtra("RequestID",RequestID);
        startActivity(intent);
    }

}
class MyBrowseRequestAdapter extends BaseAdapter {
    private Context context;
    ArrayList<Request> request;
    int layoutResourseId;

    MyBrowseRequestAdapter(Context context,ArrayList<Request> request){
        this.request=request;
        this.context=context;
    }
    public MyBrowseRequestAdapter(Context context, int activity_single_request, ArrayList<Request> request)
    {
        this.request=request;
        this.context=context;
        this.layoutResourseId=activity_single_request;
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
