package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class VolunteerViewInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView car,name;
    CircleImageView circleImageView;
    ImageView imageView;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_view_info);
        name=findViewById(R.id.UserNameVv);
        car=findViewById(R.id.car);
        circleImageView=findViewById(R.id.colo);
        imageView=findViewById(R.id.UserImage);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle intent1 = getIntent().getExtras();

        if (intent1 != null) {
            final String VolID = (String) intent1.getSerializable("Volunteers");
            DocumentReference documentReference = db.collection("Volunteers").document(VolID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>(){

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    name.setText(documentSnapshot.getString("UserName"));
                    car.setText(documentSnapshot.getString("Vehicle"));
                    getPicturePath(VolID);
                }
            });

            /*DocumentReference documentReference2 = db.collection("profilePicture").document(VolID);
            documentReference2.addSnapshotListener(this, new EventListener<DocumentSnapshot>(){

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    String photo=documentSnapshot.getString("link");
                    if(photo!=null){
                    Uri link = Uri.parse(photo);
                        Toast.makeText(VolunteerViewInfo.this, "your photo "+link , Toast.LENGTH_SHORT).show();


                    circleImageView.setImageURI(link);
                    //Picasso.with(VolunteerViewInfo.this).load(link).into(imageView);
                    }




                }
            });*/


        }


    }
    public void getPicturePath(String Vid){
        String ImageName = Vid+".png";

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference mainRef = firebaseStorage.getReference("Images");
        final File file = new File(getFilesDir(), ImageName);
        //Toast.makeText(DonatorViewInfo.this, "photo : "+file, Toast.LENGTH_SHORT).show();

        mainRef.child(ImageName).getFile(file).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uri = Uri.parse(file.toString());
                    //Toast.makeText(VolunteerViewInfo.this, "photo in: "+uri, Toast.LENGTH_SHORT).show();
                    circleImageView.setImageURI(uri);
                    circleImageView.requestLayout();

                }
            }
        });
        //return uri;
    }
    public void OpenDonatorNoti(View view) {
        startActivity(new Intent(VolunteerViewInfo.this,DonatorNotifications.class));
    }
}
