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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
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
    Notification notify;
    ListView listViewNoti;
    ListView listViewNoti2;
    ArrayList<Notification> notificarion;
    //Review review;
    //ArrayList<Review> reviewList;
    MyNotificationsAdapter myNotificationsAdapter;
    ArrayList<Uri> userIDS;
    TextView textView;



    //Abeer
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_notifications);
        listViewNoti = findViewById(R.id.list_Requestnoti);
        //listViewNoti2 = findViewById(R.id.list_Requestnoti2);
        notificarion = new ArrayList<Notification>();
        //reviewList = new ArrayList<Review>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserID = mAuth.getCurrentUser().getUid();
        userIDS = new ArrayList<Uri>();
        //textView=findViewById(R.id.textView2);

       /* String dataMessage=getIntent().getStringExtra("message");
        String fromMessage=getIntent().getStringExtra("from_user_id");
        textView.setText("Frome: "+fromMessage+" Message: "+dataMessage);*/



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

        Query q1 = db.collection("users").document(UserID).collection("Notification");
        q1.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final String Comment = document.getString("Comment");
                                final String Date= document.getString("Date");
                                final float Rate1 = document.getLong("Rate");
                                //final float Rate1=Float.parseFloat(rate);
                                final String Time = document.getString("Time");

                                //reqID = document.getString("RequestID");
                                final String from = document.getString("from");
                                final String typeOfEvent = document.getString("typeOfEvent");
                                final String typeOfNoti = document.getString("typeOfNoti");
                                //String VolunteerID = document.getString("VolnteerID");
                                //Uri PictureURI = getPicturePath(VolunteerID);

                                //String ImageName = from + ".png";//>
                               // FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();//>
                                //StorageReference mainRef = firebaseStorage.getReference("Images");//>
                                //final File file = new File(getFilesDir(), ImageName);//>
                                //Toast.makeText(DonatorNotifications.this,"THe FILE IS: "+file,Toast.LENGTH_SHORT).show();
                                //uri = Uri.parse(file.toString());//>
                                //if(uri!=null)
                                //userIDS.add(uri);//>
                               /* mainRef.child(ImageName).getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                                    //@Override
                                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            uri = Uri.parse(file.toString());
                                            userIDS.add(uri);
                                        }
                                    }
                                });*/

                               final String Msg;
                               if(Comment.equals("--")){
                                    Msg=typeOfNoti+" Your "+typeOfEvent+" Request";
                               }else{
                                    Msg=typeOfNoti+" You";
                               }


                                DocumentReference query = db.collection("users").document(from);

                                query.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()) {
                                                String from_Type = documentSnapshot.getString("Type");
                                                //Toast.makeText(context, "The Type Is: " + from_Type, Toast.LENGTH_SHORT).show();
                                                DocumentReference query2 = db.collection(from_Type + "s").document(from);
                                                query2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot documentSnapshot = task.getResult();
                                                            if (documentSnapshot.exists()) {
                                                               String from_name = documentSnapshot.getString("UserName");
                                                                //Toast.makeText(context, "The Name Is: " + from_name, Toast.LENGTH_SHORT).show();
                                                                notify = new Notification(Comment,Date,Rate1,Time, from, typeOfEvent, typeOfNoti, Msg,from_name);
                                                                notificarion.add(notify);
                                                                myNotificationsAdapter = new MyNotificationsAdapter(DonatorNotifications.this, R.layout.activity_single_notification, notificarion);
                                                                listViewNoti.setAdapter(myNotificationsAdapter);
                                                            } else {
                                                                String from_name = " ";
                                                            }
                                                        }
                                                    }
                                                });
                                            } else {
                                                String from_Type = " ";
                                            }
                                        }
                                    }
                                });



                                listViewNoti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        final Notification temp = (Notification) parent.getItemAtPosition(position);
                                        String VolID = temp.getFrom();
                                        Intent in = getIntent();
                                        in.putExtra("Volunteers", VolID);
                                        Intent intent = new Intent(DonatorNotifications.this, VolunteerViewInfo.class);
                                        intent.putExtra("Volunteers", in.getStringExtra("Volunteers"));
                                        startActivity(intent);
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
                                String VolID = document.getId();
                                //Toast.makeText(DonatorNotifications.this, "The User ID Is"+VolID, Toast.LENGTH_SHORT).show();
                                String name = document.getString("ByName");
                                //Toast.makeText(DonatorNotifications.this, "The name Is"+name, Toast.LENGTH_SHORT).show();
                                String com = document.getString("Comment");
                                //Toast.makeText(DonatorNotifications.this, "The Comment Is"+com, Toast.LENGTH_SHORT).show();
                                double rate = document.getDouble("Rating");
                                // Toast.makeText(DonatorNotifications.this, "The rate Is"+rate, Toast.LENGTH_SHORT).show();
                                //double rateing = Double.valueOf(rate);
                                String userID = document.getString("onUserID");
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



}



class MyNotificationsAdapter extends BaseAdapter {

    private Context context;
    ArrayList<Notification> notificarion;
    int layoutResourseId;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    CircleImageView circleImageView;
    Uri uri;






    MyNotificationsAdapter(Context context, ArrayList<Notification> notificarion) {
        this.notificarion = notificarion;
        this.context = context;
    }

    public MyNotificationsAdapter(Context context, int activity_single_notification,ArrayList<Notification> notificarion) {
        this.notificarion = notificarion;
        this.context = context;
        this.layoutResourseId = activity_single_notification;
    }


    @Override
    public int getCount() {
        return notificarion.size();
    }

    @Override
    public Object getItem(int position) {
        return notificarion.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getViewTypeCount(){
        return 2;
    }

    public int getItemViewType(int position){
        if(notificarion!=null)
            return 0;
        else return 1;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // =============================================initialization===============================================================
        View view = LayoutInflater.from(context).inflate(R.layout.activity_single_notification, null);
        String Comment =notificarion.get(position).getComment();
        float Rate =notificarion.get(position).getRate();
        final String from =notificarion.get(position).getFrom();
        String TypeOfEvent=notificarion.get(position).getEvent_Type();
        String typeOfNotify=notificarion.get(position).getNotifiarion_Type();
        String from_name=notificarion.get(position).getFrom_name();
        String msg=notificarion.get(position).getMessage();
        TextView volunteerName = (TextView) view.findViewById(R.id.requests);
        //LinearLayout linearLayout=view.findViewById(R.id.cont);
        //TextView com=view.findViewById(R.id.commenter_name);
        //RatingBar ratingBar=view.findViewById(R.id.rate_star);
        //TextView review_dec=view.findViewById(R.id.tv_desc_review);
        final SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(from_name+" ");
      //===================================Request===================================
        if(!typeOfNotify.equals("Review")) {
            if (typeOfNotify.equals("Pending")) {
                SpannableString state1 = new SpannableString(typeOfNotify);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#FB8C00")), 0, typeOfNotify.length(), 0);
                builder.append(state1);
            } else if (typeOfNotify.equals("Accepted")) {
                SpannableString state1 = new SpannableString(typeOfNotify);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")), 0, typeOfNotify.length(), 0);
                builder.append(state1);
            } else if (typeOfNotify.equals("Cancelled")) {
                SpannableString state1 = new SpannableString(typeOfNotify);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#BF360C")), 0, typeOfNotify.length(), 0);
                builder.append(state1);
            } else if (typeOfNotify.equals("Delivered")) {
                SpannableString state1 = new SpannableString(typeOfNotify);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#0392cf")), 0, typeOfNotify.length(), 0);
                builder.append(state1);
            }
            SpannableString TypeOfEvent1 = new SpannableString(TypeOfEvent);
            StyleSpan boldStyle=new StyleSpan(Typeface.BOLD);
            TypeOfEvent1.setSpan(boldStyle,0,TypeOfEvent.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            builder.append(" Your " + TypeOfEvent1 + " Request");

            volunteerName.setText(builder, TextView.BufferType.SPANNABLE);
            // ==================================Review========================================
        }else{
            if(Rate>0 && Rate<=1){
                String RateS=Rate+"";
                SpannableString state1=new SpannableString(RateS);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#4CAF50")),0,RateS.length(), 0);
                builder.append(" You got a new "+state1+" star review ");
            }
            else if(Rate>1 && Rate<=2){
                String RateS=Rate+"";
                SpannableString state1=new SpannableString(RateS);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#AED581")),0,RateS.length(), 0);
                builder.append(" You got a new "+state1+" star review ");
            }
            else if(Rate>2 && Rate<=3){
                String RateS=Rate+"";
                SpannableString state1=new SpannableString(RateS);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#FDD835")),0,RateS.length(), 0);
                builder.append(" You got a new "+state1+" star review ");
            }else if(Rate>3 && Rate<=4){
                String RateS=Rate+"";
                SpannableString state1=new SpannableString(RateS);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#69F0AE")),0,RateS.length(), 0);
                builder.append(state1);
            }else if(Rate>4 && Rate<=5){
                String RateS=Rate+"";
                SpannableString state1=new SpannableString(RateS);
                state1.setSpan(new ForegroundColorSpan(Color.parseColor("#D84315")),0,RateS.length(), 0);
                builder.append(" You got a new "+state1+" star review ");
            }
            builder.append(" and Comment: "+Comment);
            volunteerName.setText(builder, TextView.BufferType.SPANNABLE);

        }


       // builder.append(Comment);
        //builder.append(Rate);
       // builder.append(from);

       // builder.append(msg);



        //Uri id = userIDS.get(position);>>>
        //builder.append(" This is Id For Uri: "+id);
        //String id2=userIDSTri.get(position);
        // builder.append(" This is Id For String: "+id2);
            /*if(id!=null)
            circleImageView.setImageURI(id);
            */
        circleImageView = (CircleImageView) view.findViewById(R.id.colo1);

        /*if(id!=null)
            circleImageView.setImageURI(id);>>>*/
/*==============================photo========================================
            String ImageName = from+".png";
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference mainRef = firebaseStorage.getReference("Images");
            final File file = new File(context.getFilesDir(), ImageName);

            mainRef.child(ImageName).getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        uri = Uri.parse(file.toString());
                        circleImageView.setImageURI(uri);
                        circleImageView.requestLayout();

                    }
                }
            });
================================================================================*/




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

