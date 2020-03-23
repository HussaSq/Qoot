package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.awt.font.*;
import java.awt.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonatorNotifications extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String UserID, reqID;
    Request MAGIC;
    ListView listViewNoti;
    ListView listViewNoti2;
    ArrayList<Request> request;
    Review review;
    ArrayList<Review> reviewList;
    MyNotificationsAdapter myRequestAdapter;
    ArrayList<Uri> userIDS;

    //Abeer
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_notifications);
        listViewNoti=findViewById(R.id.list_Requestnoti);
        listViewNoti2=findViewById(R.id.list_Requestnoti2);
        request=new ArrayList<Request>();
        reviewList=new ArrayList<Review>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        userIDS = new ArrayList<Uri>();



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.notifi_don);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.Req_don:
                        startActivity(new Intent(getApplicationContext(), DonatorRequests.class));
                        overridePendingTransition(0, 0);
                        return false;

                    case R.id.notifi_don:

                        return true;

                    case R.id.prfile_don:
                        startActivity(new Intent(getApplicationContext(), DonatorProfile.class));
                        overridePendingTransition(0, 0);
                        return false;

                }
                return false;
            }
        });

        //.whereEqualTo("State"," Accepted || Cancelled")

        Query q1 = db.collection("Requests").whereEqualTo("DonatorID", UserID).whereIn("State", Arrays.asList("Accepted", "Cancelled", "Delivered"));
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String State = document.getString("State");
                                String Event = document.getString("TypeOfEvent");
                                reqID = document.getString("RequestID");
                                String REQTYPE = document.getString("RequestType");
                                String DonatorName = document.getString("DonatorName");
                                String VolunteerName = document.getString("VolnteerName");
                                String VolunteerID = document.getString("VolnteerID");
                                //Uri PictureURI = getPicturePath(VolunteerID);

                                if (VolunteerID.equals("--"))
                                    continue;
                                if (VolunteerName.equals("--"))
                                    continue;
                                String ImageName = VolunteerID + ".png";
                                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                                StorageReference mainRef = firebaseStorage.getReference("Images");
                                final File file = new File(getFilesDir(), ImageName);
                                //Toast.makeText(DonatorNotifications.this,"THe FILE IS: "+file,Toast.LENGTH_SHORT).show();
                                uri = Uri.parse(file.toString());
                                //if(uri!=null)
                                userIDS.add(uri);
                               /* mainRef.child(ImageName).getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                    //@Override
                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            uri = Uri.parse(file.toString());
                                            userIDS.add(uri);
                                        }
                                    }

                                });*/


                                MAGIC = new Request(Event, State, mAuth.getCurrentUser().getUid(), reqID, REQTYPE, DonatorName, VolunteerName);
                                request.add(MAGIC);
                               // myRequestAdapter = new MyNotificationsAdapter(DonatorNotifications.this, R.layout.activity_single_notification, request, reqID, userIDS);
                                listViewNoti.setAdapter(myRequestAdapter);
                                listViewNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final Request temp = (Request) parent.getItemAtPosition(position);
                                        DocumentReference VolRef=db.collection("Requests").document(temp.getID());
                                        VolRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String VolID = documentSnapshot.getString("VolnteerID");
                                                if (!VolID.equals("--")) {
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





       /* Query q2 = db.collection("Reviews").whereEqualTo("onUserID",UserID).whereEqualTo("RequestId",reqID);
        q2.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String comment=document.getString("Comment");
                                String commenter=document.getString("CommenterID");
                                double rate= document.getDouble("Rating");
                                String reqID=document.getString("RequestId");
                                String userDID=document.getString("onUserID");
                                review=new Review(userDID,commenter,comment,rate);
                                reviewList.add(review);
                                //listViewNoti.setAdapter(myRequestAdapter);
                                myRequestAdapter=new MyNotificationsAdapter(DonatorNotifications.this,R.layout.activity_single_notification,null,reviewList,reqID);
                                listViewNoti.setAdapter(myRequestAdapter);
                            }
                        }else{

                        }
                    }
                });*/

        /*Query q2 = db.collection("Reviews").whereEqualTo("onUserID", UserID);
        //Toast.makeText(DonatorNotifications.this, "The User ID Is"+UserID, Toast.LENGTH_SHORT).show();
        q2.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String VolID=document.getId();
                                //Toast.makeText(DonatorNotifications.this, "The User ID Is"+VolID, Toast.LENGTH_SHORT).show();
                                String name = document.getString("ByName");
                                //Toast.makeText(DonatorNotifications.this, "The name Is"+name, Toast.LENGTH_SHORT).show();
                                String com = document.getString("Comment");
                                //Toast.makeText(DonatorNotifications.this, "The Comment Is"+com, Toast.LENGTH_SHORT).show();
                               // double rate=document.getDouble("Rating");
                                // Toast.makeText(DonatorNotifications.this, "The rate Is"+rate, Toast.LENGTH_SHORT).show();
                                //double rateing = Double.valueOf(rate);
                                String userID=document.getString("onUserID");
                                //Toast.makeText(DonatorNotifications.this, "The userID Is"+userID, Toast.LENGTH_SHORT).show();
                                //String reqId=document.getString("reqID");
                                //review = new Review(userID, name, com, rate);
                                //Toast.makeText(DonatorNotifications.this, "The Name of review Is"+review.getByName(), Toast.LENGTH_SHORT).show();

                                reviewList.add(review);
                                MyNotificationsAdapter2 myRequestAdapter = new MyNotificationsAdapter2(DonatorNotifications.this, R.layout.activity_single_notification, reviewList, VolID);
                                listViewNoti2.setAdapter(myRequestAdapter);
                            }

                        } else {

                        }
                    }
                });*/


    }


    public void getPicturePath(String Vid) {
        String ImageName = Vid + ".png";
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference mainRef = firebaseStorage.getReference("Images");
        final File file = new File(getFilesDir(), ImageName);
        mainRef.child(ImageName).getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            //@Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uri = Uri.parse(file.toString());
                }
            }
        });
        //return uri;
    }

}



    class MyNotificationsAdapter extends BaseAdapter {

        private Context context;
        ArrayList<Request> request;
        //ArrayList<Review> reviews;
        int layoutResourseId;
        String reqID;
        FirebaseAuth mAuth;
        FirebaseFirestore db;
        String VolunteerName;
        String UserID;
        String type;
        //Uri PictureURI;
        ArrayList<Uri> userIDS;



        MyNotificationsAdapter(Context context, ArrayList<Request> request) {
            this.request = request;
            this.context = context;
        }

        public MyNotificationsAdapter(Context context, int activity_single_notification, ArrayList<Request> request, String reqID, ArrayList<Uri> userIDS) {
             /*if (request != null) {
                this.request = request;
            }
           if (reviews != null) {
                this.reviews = reviews;
            }*/
            this.request = request;
            this.context = context;
            this.layoutResourseId = activity_single_notification;
            this.reqID = reqID;
            this.userIDS = userIDS;



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
        //String VolID=request.get(position).;
        String state = request.get(position).Status;
        String EventType = " Your " + request.get(position).EventType + " Request";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString volunteer1 = new SpannableString(volunteer);

        //ForegroundColorSpan foregroundColorSpan=new ForegroundColorSpan(Color.parseColor("#FB8C00"));
        // volunteer1.setSpan(new ForegroundColorSpan(Color.RED),0,volunteer.length(), 0);
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
            } else if (state.equals("Delivered")) {
                SpannableString state1 = new SpannableString(state);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#0392cf")), 0, state.length(), 0);
                builder.append(state1);
            }

            SpannableString EventType1 = new SpannableString(EventType);
            builder.append(EventType1);
            TextView volunteerName = (TextView) view.findViewById(R.id.requests);

        volunteerName.setText(builder, TextView.BufferType.SPANNABLE);
        ImageView volunteerPicture = view.findViewById(R.id.colo1);
        //circleImageView.setImageURI(getPhoto());


            return view;
        }

    }


class MyNotificationsAdapter2 extends BaseAdapter {

    private Context context;
    ArrayList<Review> reviews;
    int layoutResourseId;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String VolunteerID;
    String UserID;




    MyNotificationsAdapter2(Context context,ArrayList<Review> reviews){
        this.reviews=reviews;
        this.context=context;
    }

    public MyNotificationsAdapter2(Context context, int activity_review_notifications, ArrayList<Review> reviews,String VolID) {

        this.reviews=reviews;
        this.context=context;
        this.layoutResourseId=activity_review_notifications;
        this.VolunteerID=VolID;

        //this.VolunteerName=VolunteerName;


    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getVolunteerID() {
        return VolunteerID;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos=position;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_single_notification, null);

        String volunteer= getVolunteerID()+" Add Review To you ";
        String Comment=" Comment: " +reviews.get(position).comment+" Rate: " ;
        double Rate=reviews.get(position).rate;
        SpannableStringBuilder builder=new SpannableStringBuilder();
        SpannableString volunteer1=new SpannableString(volunteer);

        builder.append(volunteer1);

        if(Rate>=0 && Rate<=1){
            String RateS=Rate+"";
            SpannableString state1=new SpannableString(RateS);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")),0,RateS.length(), 0);
            builder.append(state1);
        }
        else if(Rate>1 && Rate<=2){
            String RateS=Rate+"";
            SpannableString state1=new SpannableString(RateS);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#AED581")),0,RateS.length(), 0);
            builder.append(state1);
        }
        else if(Rate>2 && Rate<=3){
            String RateS=Rate+"";
            SpannableString state1=new SpannableString(RateS);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#FDD835")),0,RateS.length(), 0);
            builder.append(state1);
        }else if(Rate>3 && Rate<=4){
            String RateS=Rate+"";
            SpannableString state1=new SpannableString(RateS);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#69F0AE")),0,RateS.length(), 0);
            builder.append(state1);
        }else if(Rate>4 && Rate<=5){
            String RateS=Rate+"";
            SpannableString state1=new SpannableString(RateS);
            state1.setSpan(new ForegroundColorSpan(Color.parseColor("#D84315")),0,RateS.length(), 0);
            builder.append(state1);
        }

        TextView volunteerName=(TextView) view.findViewById(R.id.requests);

        volunteerName.setText(builder, TextView.BufferType.SPANNABLE);

        return view;
    }
}
