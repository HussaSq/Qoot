package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class VolunteerProfile extends AppCompatActivity {


    private TextView Username;
    private ImageView Photo;

    private Button logOut ;
    private ImageView Editprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_profile);
    }

    public void OpenEditProfilePage(View view){
        startActivity(new Intent(VolunteerProfile.this,EditVolunteerProfile.class));
    }

    public void OpenLogOut(View view){

        // do stuff for log out
        startActivity(new Intent(VolunteerProfile.this,LogIn.class));
    }
}
