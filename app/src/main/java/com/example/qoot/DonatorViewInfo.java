package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
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

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class DonatorViewInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView car,name;
    CircleImageView circleImageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donator_view_info);
        name = findViewById(R.id.UserNameVv);
        car = findViewById(R.id.car);
        circleImageView = findViewById(R.id.colo);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle intent1 = getIntent().getExtras();

        if (intent1 != null) {
            final String VolID = (String) intent1.getSerializable("Donators");
            DocumentReference documentReference = db.collection("Donators").document(VolID);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    name.setText(documentSnapshot.getString("UserName"));
                    getPicturePath(VolID);
                }
            });

        }
    }

        public void getPicturePath(String Vid){
            String ImageName = Vid+".png";

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference mainRef = firebaseStorage.getReference("Images");
            final File file = new File(getFilesDir(), ImageName);

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
        }


    public void OpenVolunteerNoti(View view) {

            startActivity(new Intent(DonatorViewInfo.this,volunteer_notification.class));

    }

    public void OpenAllComments(View view) {
        Intent intentC = new Intent(DonatorViewInfo.this, DonatorAllComments.class);
        //intentC.putExtra("MyUserId",intentC.getStringExtra("MyUserId"));
        startActivity(intentC);
    }
}
