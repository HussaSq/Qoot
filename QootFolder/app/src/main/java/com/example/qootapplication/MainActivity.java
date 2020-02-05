package com.example.qootapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
    }

    public void OpenSignupAsPage(View view) {
        startActivity(new Intent(MainActivity.this,SignUpAs.class));
    }

    public void openForPassPage(View view) {
        startActivity(new Intent(MainActivity.this,ForgotPassword.class));
    }

    public void OpenSignupPage(View view) {
        //for Test
        startActivity(new Intent(MainActivity.this,VolunteerProfileActivity.class));
    }
}

