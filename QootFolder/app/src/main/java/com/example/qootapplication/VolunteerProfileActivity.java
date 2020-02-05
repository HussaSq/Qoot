package com.example.qootapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VolunteerProfileActivity extends AppCompatActivity {


    private TextView NameTextView , UserNameTextView ;
    private ImageView UserImageView;


    FirebaseAuth MyAuth;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_volunteer_profile);
        MyAuth = FirebaseAuth.getInstance();

        // get from text
        UserNameTextView = findViewById(R.id.UserName);
        NameTextView = findViewById(R.id.Name);
        UserImageView = findViewById(R.id.UserImage);

       // loading ....
        loadUserInformation();
    }

    @Override
    protected void onStart() {
       super.onStart();
       if (MyAuth.getCurrentUser() == null){
           finish();
           startActivity(new Intent(this,LogIn.class));
       }
    }

    private void loadUserInformation() {
        FirebaseUser user = MyAuth.getCurrentUser();


        if (user != null ) {
            //if (user.getPhotoUrl() != null){_
            //  Glide.with(activity:this).load(user.getPhotoUrl().toString();).into(UserImageView);
            // }

            if (user.getDisplayName() != null) {
                NameTextView.setText(user.getDisplayName());
            }
            UserNameTextView.setText(user.getUid());

        }
    }
}