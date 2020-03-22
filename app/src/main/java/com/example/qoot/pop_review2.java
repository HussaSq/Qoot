package com.example.qoot;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class pop_review2 extends Activity {

    EditText Comment;
    RatingBar Rate;
    Button send;
    TextView close;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userID, ReqIDDD, on_user;
    String comment;
    double rate;
    Bundle myIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_review2);

        DisplayMetrics dm= new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        // curve
        WindowManager.LayoutParams params=getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-40;
        getWindow().setAttributes(params);

        getWindow().setLayout((int)(width*.85),(int)(height*.5));


        Comment = findViewById(R.id.review);
        Rate = findViewById(R.id.rate_bar);
        send = findViewById(R.id.send_review);
        close=findViewById(R.id.txtclose);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //name = Name.getText().toString();
                comment = Comment.getText().toString();
                rate = ((double) Rate.getRating());

                /*if (TextUtils.isEmpty(name)) {
                    Name.setError("Please Enter Your Name");
                    return;
                }*/
                if (TextUtils.isEmpty(comment)) {
                    Comment.setError("Please Enter Your Comments");
                    return;
                }

                mAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();
                userID = mAuth.getCurrentUser().getUid();
                myIntent = getIntent().getExtras();
                if (myIntent != null) {
                    ReqIDDD = (String) myIntent.getSerializable("RequestID");
                }

                DocumentReference reqid = db.collection("Requests").document(ReqIDDD);
                reqid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        on_user = documentSnapshot.getString("VolnteerID");


                        /// First try to add in DB
                        //Review MyReview = new Review(on_user,name,comment,rate);
                        //DocumentReference documentReference = db.collection("Reviews").document(userID);

                        Map<String, Object> review = new HashMap<>();
                        //**********=donater id
                        review.put("CommenterID", userID);
                        review.put("onUserID", on_user);
                        review.put("Comment", comment);
                        review.put("Rating", rate);
                        review.put("RequestId",ReqIDDD);

                        /// second try to add in DB
                        db.collection("Reviews")
                                .add(review)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText( pop_review2.this,"Thank You!",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(pop_review2.this, "Something Went Wrong,Try Again ! " , Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
                Intent i = new Intent(pop_review2.this,DonatorRequests.class);
                startActivity(i);
            }
        });



    }
}
