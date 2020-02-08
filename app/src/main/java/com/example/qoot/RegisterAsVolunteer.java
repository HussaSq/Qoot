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
import android.widget.AdapterView;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.List;


public class RegisterAsVolunteer extends AppCompatActivity {

    // text fields in the form
    EditText username, email, password;
    Spinner cars;
    Button register;
    RadioGroup GenderGroup;
    RadioButton gender;

        // variables for db
    FirebaseAuth mAuth;
    FirebaseFirestore fstore ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_volunteer);

        //initialize textViews
        username = findViewById(R.id.userText);
        email = findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);
        register = findViewById(R.id.button);
        GenderGroup = (RadioGroup) findViewById(R.id.radioGender);
        cars = findViewById(R.id.carDD);

        //initialize firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // DROP DOWN CODE
        final String[] types = new String[]{"Small", "Medium", "Truck","None"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        cars.setAdapter(adapter);


        // RADIO CODE..
        int selectedId = GenderGroup.getCheckedRadioButtonId();
        gender = (RadioButton) findViewById(selectedId);

        // checking
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString().trim();
                String Em = email.getText().toString().trim();
                final String passW = password.getText().toString().trim();

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

                //register user with authentication and firestore
                mAuth.createUserWithEmailAndPassword(Em, passW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterAsVolunteer.this, "Registration Was Successful!!", Toast.LENGTH_SHORT).show();

                                  // create the object
                            Volunteer vol = new Volunteer(
                                    username.getText().toString()
                                    ,email.getText().toString()
                                    ,password.getText().toString()
                                    ,cars.getSelectedItem().toString() // not sure about this
                                    ,gender.getText().toString());

                                // now add this to firebase
                                fstore= FirebaseFirestore.getInstance();
                                String v = mAuth.getCurrentUser().getUid();
                                fstore.collection("users").document(v).set(vol);
                                // now go to the volunteer profile activity

                                                                // this have to change v
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