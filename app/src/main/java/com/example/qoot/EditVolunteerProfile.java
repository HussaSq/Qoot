package com.example.qoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EditVolunteerProfile extends AppCompatActivity {

    Spinner cars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_volunteer_profile);

        // DROP DOWN CODE
        cars = findViewById(R.id.carDD);
        final String[] types = new String[]{"Small", "Medium", "Truck","None"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        cars.setAdapter(adapter);
        // I think here we need to fetch the type from DB.. not like the above
    }

    public void OpenProfile(View view) {
        startActivity(new Intent(EditVolunteerProfile.this,VolunteerProfile.class));
    }

}


