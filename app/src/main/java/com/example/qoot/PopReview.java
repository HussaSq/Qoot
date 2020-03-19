package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PopReview extends Activity {

    EditText Name;
    EditText Comment;
    RatingBar Rate;
    Button send ;
    Button notNow;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String userID, ReqIDDD, on_user;
    String name, comment;
    double rate;
    Bundle myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_review);

        Name = findViewById(R.id.et_name);
        Comment = findViewById(R.id.review);
        Rate = findViewById(R.id.rate_star);
        send = findViewById(R.id.btn_send_review);
        notNow = findViewById(R.id.btn_not_now);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Name.getText().toString();
                comment = Comment.getText().toString();
                rate = ((double) Rate.getRating());


                if (TextUtils.isEmpty(name)) {
                    Name.setError("Please Enter Your Name");
                    return;
                }
                if (TextUtils.isEmpty(comment)) {
                    Comment.setError("Please Enter Your comments");
                    return;
                }

                mAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();
                userID = mAuth.getCurrentUser().getUid();
                myIntent = getIntent().getExtras();
                if (myIntent != null) {
                    ReqIDDD = (String) myIntent.getSerializable("RequestID");}

                DocumentReference reqid = db.collection("Requests").document(ReqIDDD);
                reqid.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        on_user = documentSnapshot.getString("DonatorID");
                    }
                });

                Review MyReview = new Review(on_user,name,comment,rate);
                DocumentReference documentReference = db.collection("Reviews").document(userID);

                Map<String,Object> review = new HashMap<>();
                review.put("onUserID",MyReview.onUserID);
                review.put("ByName",MyReview.byName);
                review.put("Comment",MyReview.comment);
                review.put("Rating",MyReview.rate);

                /*
                db.collection("Reviews")
                        .add(review)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                  @Override
                                                  public void onSuccess(DocumentReference documentReference) {
                                                      documentReference.update("RequestID", documentReference.getId()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                          @Override
                                                          public void onSuccess(Void aVoid) {

                                                              //  Toast.makeText( EditDonatorProfile.this,"user updated",Toast.LENGTH_SHORT).show();
                                                          }
                                                      });
                                                  }
                                              });
                */
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int hight = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(hight*.5));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x =0;
        params.y = -40;
        getWindow().setAttributes(params);



    }
}

class Review{
    String onUserID;
    String byName;
    String comment;
    double rate;

    public Review(String onUserID, String byName, String comment, double rate) {
        this.onUserID = onUserID;
        this.byName = byName;
        this.comment = comment;
        this.rate = rate;
    }

    public Review(String onUserID) {
        this.onUserID = onUserID;
    }


    public String getOnUserID() {
        return onUserID;
    }

    public void setOnUserID(String onUserID) {
        this.onUserID = onUserID;
    }

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
