package com.example.qootapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    //Just For Testing
    public void OpenReg(View view) {


        startActivity(new Intent(MainActivity.this,RegisterAsDonator .class));

    }// till here
}

