package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.annotation.Nullable;

public class DonatorProfile extends AppCompatActivity {
     TextView Username,warnM;
     ImageView Photo,warn;
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

        Username = findViewById(R.id.UserNameD);
        Photo = findViewById(R.id.UserImage);
        linearLayout = findViewById(R.id.valid);
        root = findViewById(R.id.rootProfile);
        //warnM = findViewById(R.id.warnMess);
        mAuth = FirebaseAuth.getInstance();
        mfStore = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        final String userId=mAuth.getCurrentUser().getUid();
         user = mAuth.getCurrentUser();
        if(!user.isEmailVerified()){
            root.removeView(linearLayout);
            //linearLayout.setVisibility(View.VISIBLE);
            //warnM.setVisibility(View.VISIBLE);
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

            }
        });

        DocumentReference documentReference1 =db.collection("profilePicture").document(userId);
        documentReference1.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String uri = documentSnapshot.getString("link");
             // StorageReference storageReference = StorageReference.getReference("Images/"+userId+".png");
             //   StorageReference mImageRef = FirebaseStorage.getInstance().getReference("Images/"+userId+".png");
            //   Toast.makeText(DonatorProfile.this," Link "+uri,Toast.LENGTH_LONG).show();
//              if (mImageRef == null){
//                  return;
//              }
                if(uri == null)
                  return;
                       Uri link = Uri.parse(String.valueOf(uri));
                       Picasso.with(DonatorProfile.this).load(link).into(Photo);
//                Toast.makeText(DonatorProfile.this," Link "+link,Toast.LENGTH_LONG).show();
//
//                //  uri =  uri.substring(uri.indexOf('.'));
//              /// mfStore.getFile(new File("Images/" + userId + "." + uri + ""));
//                    Picasso.with(DonatorProfile.this).load(link).into(Photo);
//              //  Photo.setImageURI(link);

            }
        });

        // Create a storage reference from our app




//        mfStore.child("Images/"+userId+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//
//                Uri downloadUri = taskSnapshot.getMetadata().getDownloadUrl();
//                generatedFilePath = downloadUri.toString(); /// The string(file link) that you need
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });

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