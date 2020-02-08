package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditVolunteerProfile extends AppCompatActivity {

    



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_volunteer_profile);


    }

    public void OpenProfile(View view) {
        startActivity(new Intent(EditVolunteerProfile.this,VolunteerProfile.class));
    }

}


