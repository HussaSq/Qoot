package com.example.qoot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;


public class RegisterAsVolunteer extends AppCompatActivity {
    EditText username, email, password;
    Spinner cars;
    Button register;
    RadioGroup GenderGroup;
    RadioButton gender;
    TextView mDisplayDate;
    //DatePickerDialog.OnDateSetListener mDateSetListener;
   //private static final String TAG = "RegisterAsVolunteer";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_volunteer);
        username = findViewById(R.id.userText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        register = findViewById(R.id.button);
        GenderGroup = (RadioGroup) findViewById(R.id.radioGender);
        //mDisplayDate = (TextView) findViewById(R.id.Dob);
        cars = findViewById(R.id.carDD);
        mAuth = FirebaseAuth.getInstance();


        /*//  DOB CODE
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RegisterAsVolunteer.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                // Above 18 ..
                mDisplayDate.setText(date);
                int y = date.lastIndexOf('/');
                int choose = Integer.parseInt(date.substring(y+1));
                if((year - choose) <18){
                    return;}
            }
        }; */

        // DROP DOWN CODE
        final String[] types = new String[]{"Small", "Medium", "Truck","None"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        cars.setAdapter(adapter);


        // RADIO CODE..
        int selectedId = GenderGroup.getCheckedRadioButtonId();
        gender = (RadioButton) findViewById(selectedId);
        Toast.makeText(RegisterAsVolunteer.this, gender.getText(), Toast.LENGTH_SHORT).show();
        /////////

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String Em = email.getText().toString().trim();
                String passW = password.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    username.setError("Please Enter Username, It is Required");
                    return;
                }
                if (TextUtils.isEmpty(Em)) {
                    email.setError("Please Enter Your Email, It is Required");
                    return;
                }
                if (TextUtils.isEmpty(passW)) {
                    password.setError("Please Enter Password, It is Required");
                    return;
                }

                if (passW.length() < 8) {
                    password.setError("The Characters Must Be At Least 8 Characters ");
                    return;
                }
                //register user
                mAuth.createUserWithEmailAndPassword(Em, passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterAsVolunteer.this, "Registration Was Successful!!", Toast.LENGTH_SHORT).show();
                            //we MUST CHANGE THIS to THE HOME PAGE
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(RegisterAsVolunteer.this, "Something Went Wrong ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void OpenSignupAsPage(View view) {
        startActivity(new Intent(RegisterAsVolunteer.this,SignUpAs.class));
    }

    public void OpenSignInPage(View view) {
        startActivity(new Intent(RegisterAsVolunteer.this,LogIn.class));
    }
}