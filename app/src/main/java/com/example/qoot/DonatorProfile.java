package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class DonatorProfile extends AppCompatActivity {

    TextView Username;
    ImageView Photo;
    TextView numDona ;
    TextView TextRate;
    LinearLayout linearLayout;
    ConstraintLayout root ;
    // eventually we will add comments and ratings as well
    FirebaseAuth mAuth ;
    FirebaseFirestore db;
    StorageReference mfStore;
    FirebaseUser user ;
    public static final String TAG = "DonatorProfile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_profile);
        BottomNavigationView bottomNavigationView =findViewById(R.id.bottom_navigation_don);
        bottomNavigationView.setSelectedItemId(R.id.prfile_don);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.notifi_don:
                        startActivity(new Intent(getApplicationContext(),DonatorNotifications.class));
                        overridePendingTransition(0,0);
                        return false;

                    case R.id.prfile_don:
                        return true;

                    case R.id.Req_don:
                        startActivity(new Intent(getApplicationContext(),DonatorRequests.class));
                        overridePendingTransition(0,0);
                        return false;
                }
                return false;
            }
        });
        numDona=findViewById(R.id.Donations);
        TextRate=findViewById(R.id.RateD);
        Username = findViewById(R.id.UserNameD);
        Photo = findViewById(R.id.UserImage);
        linearLayout = findViewById(R.id.valid);
        root = findViewById(R.id.rootProfile);
        mAuth = FirebaseAuth.getInstance();
        mfStore = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        final String userId=mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        Query q1 = db.collection("Requests").whereEqualTo("DonatorID",userId).whereEqualTo("State","Delivered");
        q1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            int don=0;
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        don += 1; }
                    numDona.setText(""+don);
                } else {
                }
            }
        });







                        if(!user.isEmailVerified()){
            root.removeView(linearLayout);
            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DonatorProfile.this, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"OnFailure: Email Not Sent");
                }
            });
        }
        DocumentReference documentReference =db.collection("Donators").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Username.setText(documentSnapshot.getString("UserName"));
                numDona.setText(documentSnapshot.getString("numDona"));
                TextRate.setText(documentSnapshot.getString("TextRate"));

            }
        });
        String user = mAuth.getCurrentUser().getUid();
        download(user+".png");
            }
    private void download(String imageName) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference mainRef = firebaseStorage.getReference("Images");
        final File file = new File(getFilesDir(), imageName);
        mainRef.child(imageName).getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                //Toast.makeText(DonatorProfile.this, "photo : "+file, Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {

                  Uri u =Uri.parse(file.toString());
                    Photo.setImageURI(u);
                    Photo.requestLayout();
                    //Photo.getLayoutParams().height = 400;
                    //Photo.getLayoutParams().width = 400;
                } else {

                }
            }
        });
    }









    public void OpenEditProfilePage(View view){
        startActivity(new Intent(DonatorProfile.this,EditDonatorProfile.class));
    }
    public void OpenLogOut(View view) {
        Toast.makeText(DonatorProfile.this, "log out Was Successful!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DonatorProfile.this,LogIn.class);
        startActivity(intent);
    }

}

class Review{
    String commenterName;
    String comment;
    float rate;


    public Review(String commenterName, String comment, float rate) {
        this.commenterName = commenterName;
        this.comment = comment;
        this.rate = rate;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}


